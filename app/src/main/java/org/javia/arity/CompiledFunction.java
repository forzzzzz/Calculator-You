/*
 * Copyright (C) 2007-2009 Mihai Preda.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.javia.arity;

import com.forz.calculator.old_arch.calculator.CalculatorViewModel;

import java.util.Random;

/**
 * CompiledFunction is a function that was parsed from a string expression.
 * It is represented as a sequence of bytecodes which are executed in order to
 * evaluate the function.
 *
 * <h3>Thread safety</h3>
 * CompiledFunction instances are thread-safe (don't require external locking),
 * By default the evaluation is globally serialized
 * (it doesn't take advantage of multiple threads).<p>
 * <p>
 * You can achive parallel evaluation by creating an instance of EvalContext
 * for each thread, and passing the EvalContext as the last parameter of the
 * eval() methods.
 */



public class CompiledFunction extends ContextFunction {
    private static final IsComplexException IS_COMPLEX = new IsComplexException();
    private static final Random random = new Random();
    private static final double[] EMPTY_DOUBLE = {};
    private static final Function[] EMPTY_FUN = {};
    private static final Complex ONE_THIRD = new Complex(1 / 3., 0);

    private final double constsRe[], constsIm[];

    private final Function funcs[];
    private final byte code[];
    private final int arity; // >= 0
    private final boolean isDegreeModActivated = Boolean.TRUE.equals(CalculatorViewModel.INSTANCE.isDegreeModActivated().getValue());

    CompiledFunction(int arity, byte[] code, double[] constsRe, double[] constsIm, Function funcs[]) {
        this.arity = arity;
        this.code = code;
        this.constsRe = constsRe;
        this.constsIm = constsIm;
        this.funcs = funcs;
    }

    static Function makeOpFunction(int op) {
        if (VM.arity[op] != 1) {
            throw new Error("makeOpFunction expects arity 1, found " + VM.arity[op]);
        }
        CompiledFunction fun = new CompiledFunction(VM.arity[op], new byte[]{VM.LOAD0, (byte) op}, EMPTY_DOUBLE, EMPTY_DOUBLE, EMPTY_FUN);
        if (op == VM.ABS) {
            fun.setDerivative(new Function() {
                public int arity() {
                    return 1;
                }

                public double eval(double x) {
                    return x > 0 ? 1 : x < 0 ? -1 : 0;
                }
            });
        }
        return fun;
    }

    //@Override
    public int arity() {
        return arity;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        int cpos = 0, fpos = 0;
        if (arity != 0) {
            buf.append("arity ").append(arity).append("; ");
        }
        for (int i = 0; i < code.length; ++i) {
            byte op = code[i];
            buf.append(VM.opcodeName[op]);
            if (op == VM.CONST) {
                buf.append(' ');
                if (constsIm == null) {
                    buf.append(constsRe[cpos]);
                } else {
                    buf.append('(').append(constsRe[cpos]).append(", ").append(constsIm[cpos]).append(')');
                }
                ++cpos;
            } else if (op == VM.CALL) {
                ++fpos;
                //buf.append(" {").append(funcs[fpos++].toString()).append('}');
            }
            buf.append("; ");
        }
        if (cpos != constsRe.length) {
            buf.append("\nuses only ").append(cpos).append(" consts out of ").append(constsRe.length);
        }
        if (fpos != funcs.length) {
            buf.append("\nuses only ").append(fpos).append(" funcs out of ").append(funcs.length);
        }
        return buf.toString();
    }

    public double eval(double args[], EvalContext context) {
        if (constsIm != null) {
            return evalComplexToReal(args, context);
        }
        checkArity(args.length);
        System.arraycopy(args, 0, context.stackRe, context.stackBase, args.length);
        try {
            execReal(context, context.stackBase + args.length - 1);
            return context.stackRe[context.stackBase];
        } catch (IsComplexException e) {
            return evalComplexToReal(args, context);
        }
    }

    private double evalComplexToReal(double args[], EvalContext context) {
        Complex argsC[] = toComplex(args, context);
        Complex c = eval(argsC, context);
        return c.asReal();
    }

    public Complex eval(Complex args[], EvalContext context) {
        checkArity(args.length);
        Complex stack[] = context.stackComplex;
        int base = context.stackBase;
        for (int i = 0; i < args.length; ++i) {
            stack[i + base].set(args[i]);
        }
        execComplex(context, base + args.length - 1);
        return stack[base];
    }

    private int execReal(EvalContext context, int p) throws IsComplexException {
        int expected = p + 1;
        p = execWithoutCheck(context, p);
        if (p != expected) {
            throw new Error("Stack pointer after exec: expected " +
                    expected + ", got " + p);
        }
        context.stackRe[p - arity] = context.stackRe[p];
        return p - arity;
    }

    private int execComplex(EvalContext context, int p) {
        int expected = p + 1;
        p = execWithoutCheckComplex(context, p, -2);
        if (p != expected) {
            throw new Error("Stack pointer after exec: expected " +
                    expected + ", got " + p);
        }
        context.stackComplex[p - arity].set(context.stackComplex[p]);
        return p - arity;
    }

    int execWithoutCheck(EvalContext context, int p) throws IsComplexException {
        if (constsIm != null) {
            throw IS_COMPLEX;
        }

        double s[] = context.stackRe;

        int stackBase = p - arity;
        int constp = 0;
        int funp = 0;

        int codeLen = code.length;
        int percentPC = -2;
        for (int pc = 0; pc < codeLen; ++pc) {
            final int opcode = code[pc];
            switch (opcode) {
                case VM.CONST:
                    s[++p] = constsRe[constp++];
                    break;

                case VM.CALL: {
                    Function f = funcs[funp++];
                    if (f instanceof CompiledFunction) {
                        p = ((CompiledFunction) f).execReal(context, p);
                    } else {
                        int arity = f.arity();
                        p -= arity;
                        double result;
                        int prevBase = context.stackBase;
                        try {
                            context.stackBase = p + 1;
                            switch (arity) {
                                case 0:
                                    result = f.eval();
                                    break;
                                case 1:
                                    result = f.eval(s[p + 1]);
                                    break;
                                case 2:
                                    result = f.eval(s[p + 1], s[p + 2]);
                                    break;
                                default:
                                    double args[] = new double[arity];
                                    System.arraycopy(s, p + 1, args, 0, arity);
                                    result = f.eval(args);
                            }
                        } finally {
                            context.stackBase = prevBase;
                        }
                        s[++p] = result;
                        //System.out.println(": " + p + " " + s[0] + " " + s[1] + " " + s[2]);
                    }
                    break;
                }

                case VM.RND:
                    s[++p] = random.nextDouble();
                    break;

                case VM.ADD: {
                    final double a = s[--p];
                    double res = a + (percentPC == pc - 1 ? s[p] * s[p + 1] : s[p + 1]);
                    if (Math.abs(res) < Math.ulp(a) * 1024) {
                        // hack for "1.1-1-.1"
                        res = 0;
                    }
                    s[p] = res;
                    break;
                }

                case VM.SUB: {
                    final double a = s[--p];
                    double res = a - (percentPC == pc - 1 ? s[p] * s[p + 1] : s[p + 1]);
                    if (Math.abs(res) < Math.ulp(a) * 1024) {
                        // hack for "1.1-1-.1"
                        res = 0;
                    }
                    s[p] = res;
                    break;
                }

                case VM.MUL:
                    s[--p] *= s[p + 1];
                    break;
                case VM.DIV:
                    s[--p] /= s[p + 1];
                    break;
                case VM.MOD:
                    s[--p] %= s[p + 1];
                    break;

                case VM.POWER: {
                    s[--p] = Math.pow(s[p], s[p + 1]);
                    break;
                }

                case VM.UMIN:
                    s[p] = -s[p];
                    break;
                case VM.FACT:
                    s[p] = MoreMath.factorial(s[p]);
                    break;
                case VM.PERCENT:
                    s[p] = s[p] * .01;
                    percentPC = pc;
                    break;

                case VM.SIN:
                    if (isDegreeModActivated){
                        s[p] = MoreMath.sin(Math.toRadians(s[p]));
                    }else {
                        s[p] = MoreMath.sin(s[p]);
                    }
                    break;
                case VM.COS:
                    if (isDegreeModActivated){
                        s[p] = MoreMath.cos(Math.toRadians(s[p]));
                    }else {
                        s[p] = MoreMath.cos(s[p]);
                    }
                    break;
                case VM.TAN:
                    if (isDegreeModActivated){
                        s[p] = MoreMath.tan(Math.toRadians(s[p]));
                    }else {
                        s[p] = MoreMath.tan(s[p]);
                    }
                    break;

                case VM.ASIN: {
                    double v = s[p];
                    if (v < -1 || v > 1) {
                        throw IS_COMPLEX;
                    }
                    if (isDegreeModActivated){
                        s[p] = Math.toDegrees(Math.asin(v));
                    }else {
                        s[p] = Math.asin(v);
                    }
                    break;
                }

                case VM.ACOS: {
                    double v = s[p];
                    if (v < -1 || v > 1) {
                        throw IS_COMPLEX;
                    }
                    if (isDegreeModActivated){
                        s[p] = Math.toDegrees(Math.acos(v));
                    }else {
                        s[p] = Math.acos(v);
                    }
                    break;
                }

                case VM.ATAN:
                    if (isDegreeModActivated){
                        s[p] = Math.toDegrees(Math.atan(s[p]));
                    }else {
                        s[p] = Math.atan(s[p]);
                    }
                    break;

                case VM.EXP:
                    s[p] = Math.exp(s[p]);
                    break;
                case VM.LN:
                    s[p] = Math.log(s[p]);
                    break;

                case VM.SQRT: {
                    double v = s[p];
                    if (v < 0) {
                        throw IS_COMPLEX;
                    }
                    s[p] = Math.sqrt(v);
                    break;
                }

                case VM.CBRT:
                    s[p] = Math.cbrt(s[p]);
                    break;

                case VM.SINH:
                    s[p] = Math.sinh(s[p]);
                    break;
                case VM.COSH:
                    s[p] = Math.cosh(s[p]);
                    break;
                case VM.TANH:
                    s[p] = Math.tanh(s[p]);
                    break;
                case VM.ASINH:
                    s[p] = MoreMath.asinh(s[p]);
                    break;
                case VM.ACOSH:
                    s[p] = MoreMath.acosh(s[p]);
                    break;
                case VM.ATANH:
                    s[p] = MoreMath.atanh(s[p]);
                    break;

                case VM.ABS:
                    s[p] = Math.abs(s[p]);
                    break;
                case VM.FLOOR:
                    s[p] = Math.floor(s[p]);
                    break;
                case VM.CEIL:
                    s[p] = Math.ceil(s[p]);
                    break;
                case VM.SIGN: {
                    double v = s[p];
                    s[p] = v > 0 ? 1 : v < 0 ? -1 : v == 0 ? 0 : Double.NaN;
                    break;
                }

                case VM.MIN:
                    s[--p] = Math.min(s[p], s[p + 1]);
                    break;
                case VM.MAX:
                    s[--p] = Math.max(s[p], s[p + 1]);
                    break;
                case VM.GCD:
                    s[--p] = MoreMath.gcd(s[p], s[p + 1]);
                    break;
                case VM.COMB:
                    s[--p] = MoreMath.combinations(s[p], s[p + 1]);
                    break;
                case VM.PERM:
                    s[--p] = MoreMath.permutations(s[p], s[p + 1]);
                    break;

                case VM.LOAD0:
                case VM.LOAD1:
                case VM.LOAD2:
                case VM.LOAD3:
                case VM.LOAD4:
                    //System.out.println("base " + stackBase + "; p " + p + "; arity " + arity);
                    s[++p] = s[stackBase + opcode - (VM.LOAD0 - 1)];
                    break;

                case VM.REAL:
                    break; // NOP

                case VM.IMAG:
                    if (!Double.isNaN(s[p])) {
                        s[p] = 0;
                    }
                    break;

                default:
                    throw new Error("Unknown opcode " + opcode);
            }
        }
        return p;
    }

    int execWithoutCheckComplex(EvalContext context, int p, int percentPC) {
        Complex s[] = context.stackComplex;

        int stackBase = p - arity;
        int constp = 0;
        int funp = 0;

        int codeLen = code.length;
        // System.out.println("exec " + this);
        for (int pc = 0; pc < codeLen; ++pc) {
            final int opcode = code[pc];
            // System.out.println("+ " + pc + ' ' + opcode + ' ' + p);
            switch (opcode) {
                case VM.CONST:
                    ++p;
                    s[p].set(constsRe[constp], constsIm == null ? 0 : constsIm[constp]);
                    ++constp;
                    break;

                case VM.CALL: {
                    Function f = funcs[funp++];
                    if (f instanceof CompiledFunction) {
                        p = ((CompiledFunction) f).execComplex(context, p);
                    } else {
                        int arity = f.arity();
                        p -= arity;
                        Complex result;
                        int prevBase = context.stackBase;
                        try {
                            context.stackBase = p + 1;
                            switch (arity) {
                                case 0:
                                    result = new Complex(f.eval(), 0);
                                    break;
                                case 1:
                                    result = f.eval(s[p + 1]);
                                    break;
                                case 2:
                                    result = f.eval(s[p + 1], s[p + 2]);
                                    break;
                                default:
                                    Complex args[] = new Complex[arity];
                                    System.arraycopy(s, p + 1, args, 0, arity);
                                    result = f.eval(args);
                            }
                        } finally {
                            context.stackBase = prevBase;
                        }
                        s[++p].set(result);
                    }
                    break;
                }

                case VM.RND:
                    s[++p].set(random.nextDouble(), 0);
                    break;

                case VM.ADD:
                    s[--p].add(percentPC == pc - 1 ? s[p + 1].mul(s[p]) : s[p + 1]);
                    break;
                case VM.SUB:
                    s[--p].sub(percentPC == pc - 1 ? s[p + 1].mul(s[p]) : s[p + 1]);
                    break;
                case VM.MUL:
                    s[--p].mul(s[p + 1]);
                    break;
                case VM.DIV:
                    s[--p].div(s[p + 1]);
                    break;
                case VM.MOD:
                    s[--p].mod(s[p + 1]);
                    break;
                case VM.POWER:
                    s[--p].pow(s[p + 1]);
                    break;

                case VM.UMIN:
                    s[p].negate();
                    break;
                case VM.FACT:
                    s[p].factorial();
                    break;
                case VM.PERCENT:
                    s[p].mul(.01);
                    percentPC = pc;
                    break;

                case VM.SIN:
                    s[p].sin();
                    break;
                case VM.COS:
                    s[p].cos();
                    break;
                case VM.TAN:
                    s[p].tan();
                    break;
                case VM.SINH:
                    s[p].sinh();
                    break;
                case VM.COSH:
                    s[p].cosh();
                    break;
                case VM.TANH:
                    s[p].tanh();
                    break;

                case VM.ASIN:
                    s[p].asin();
                    break;
                case VM.ACOS:
                    s[p].acos();
                    break;
                case VM.ATAN:
                    s[p].atan();
                    break;
                case VM.ASINH:
                    s[p].asinh();
                    break;
                case VM.ACOSH:
                    s[p].acosh();
                    break;
                case VM.ATANH:
                    s[p].atanh();
                    break;

                case VM.EXP:
                    s[p].exp();
                    break;
                case VM.LN:
                    s[p].log();
                    break;

                case VM.SQRT:
                    s[p].sqrt();
                    break;
                case VM.CBRT:
                    if (s[p].im == 0) {
                        s[p].re = Math.cbrt(s[p].re);
                    } else {
                        s[p].pow(ONE_THIRD);
                    }
                    break;

                case VM.ABS:
                    s[p].set(s[p].abs(), 0);
                    break;
                case VM.FLOOR:
                    s[p].set(Math.floor(s[p].re), 0);
                    break;
                case VM.CEIL:
                    s[p].set(Math.ceil(s[p].re), 0);
                    break;
                case VM.SIGN: {
                    double a = s[p].re;
                    double b = s[p].im;
                    if (b == 0) {
                        s[p].set(a > 0 ? 1 : a < 0 ? -1 : a == 0 ? 0 : Double.NaN, 0);
                    } else if (!s[p].isNaN()) {
                        double abs = s[p].abs();
                        s[p].set(a / abs, b / abs);
                    } else {
                        s[p].set(Double.NaN, 0);
                    }
                    break;
                }

                case VM.MIN:
                    --p;
                    if (s[p + 1].re < s[p].re || s[p + 1].isNaN()) {
                        s[p].set(s[p + 1]);
                    }
                    break;

                case VM.MAX:
                    --p;
                    if (s[p].re < s[p + 1].re || s[p + 1].isNaN()) {
                        s[p].set(s[p + 1]);
                    }
                    break;

                case VM.GCD:
                    //s[--p] = MoreMath.gcd(s[p], s[p+1]);
                    s[--p].gcd(s[p + 1]);
                    break;

                case VM.COMB:
                    s[--p].combinations(s[p + 1]);
                    break;

                case VM.PERM:
                    s[--p].permutations(s[p + 1]);
                    break;

                case VM.LOAD0:
                case VM.LOAD1:
                case VM.LOAD2:
                case VM.LOAD3:
                case VM.LOAD4:
                    s[++p].set(s[stackBase + opcode - (VM.LOAD0 - 1)]);
                    break;

                case VM.REAL:
                    s[p].set(s[p].isNaN() ? Double.NaN : s[p].re, 0);
                    break;

                case VM.IMAG:
                    s[p].set(s[p].isNaN() ? Double.NaN : s[p].im, 0);
                    break;

                default:
                    throw new Error("Unknown opcode " + opcode);
            }
        }
        return p;
    }
}
