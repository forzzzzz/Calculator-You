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

/* Optimizing Code Generator
   Reads tokens in RPN (Reverse Polish Notation) order,
   and generates VM opcodes,
   doing constant-folding optimization.
 */

class OptCodeGen extends SimpleCodeGen {
    EvalContext context = new EvalContext();
    int sp;
    Complex stack[] = context.stackComplex;

    double traceConstsRe[] = new double[1];
    double traceConstsIm[] = new double[1];
    Function traceFuncs[] = new Function[1];
    byte traceCode[] = new byte[1];
    CompiledFunction tracer = new CompiledFunction(0, traceCode, traceConstsRe, traceConstsIm, traceFuncs);

    int intrinsicArity;
    private boolean isPercent;

    OptCodeGen(SyntaxException e) {
        super(e);
    }

    //@Override
    void start() {
        super.start();
        sp = -1;
        intrinsicArity = 0;
        isPercent = false;
    }

    //@Override
    void push(Token token) throws SyntaxException {
        // System.err.println("state " + getFun(0) + "; token " + token);
        final boolean prevWasPercent = isPercent;
        isPercent = false;
        byte op;
        switch (token.id) {
            case Lexer.NUMBER:
                op = VM.CONST;
                traceConstsRe[0] = token.value;
                traceConstsIm[0] = 0;
                break;

            case Lexer.CONST:
            case Lexer.CALL:
                Symbol symbol = getSymbol(token);
                if (token.isDerivative()) {
                    op = VM.CALL;
                    traceFuncs[0] = symbol.fun.getDerivative();
                } else if (symbol.op > 0) { // built-in
                    op = symbol.op;
                    if (op >= VM.LOAD0 && op <= VM.LOAD4) {
                        int arg = op - VM.LOAD0;
                        if (arg + 1 > intrinsicArity) {
                            intrinsicArity = arg + 1;
                        }
                        stack[++sp].re = Double.NaN;
                        stack[sp].im = 0;
                        code.push(op);
                        //System.out.println("op " + VM.opcodeName[op] + "; sp " + sp + "; top " + stack[sp]);
                        return;
                    }
                } else if (symbol.fun != null) { // function call
                    op = VM.CALL;
                    traceFuncs[0] = symbol.fun;
                } else { // variable reference
                    op = VM.CONST;
                    traceConstsRe[0] = symbol.valueRe;
                    traceConstsIm[0] = symbol.valueIm;
                }
                break;

            default:
                op = token.vmop;
                if (op <= 0) {
                    throw new Error("wrong vmop: " + op);
                }
                if (op == VM.PERCENT) {
                    isPercent = true;
                }
        }
        int oldSP = sp;
        traceCode[0] = op;
        if (op != VM.RND) {
            sp = tracer.execWithoutCheckComplex(context, sp, prevWasPercent ? -1 : -2);
        } else {
            stack[++sp].re = Double.NaN;
            stack[sp].im = 0;
        }

        //System.out.println("op " + VM.opcodeName[op] + "; old " + oldSP + "; sp " + sp + "; top " + stack[sp] + " " + stack[0]);

        //constant folding
        if (!stack[sp].isNaN() || op == VM.CONST) {
            int nPopCode = op == VM.CALL ? traceFuncs[0].arity() : VM.arity[op];
            while (nPopCode > 0) {
                final byte pop = code.pop();
                if (pop == VM.CONST) {
                    consts.pop();
                } else if (pop == VM.CALL) {
                    Function f = funcs.pop();
                    nPopCode += f.arity() - 1;
                } else {
                    nPopCode += VM.arity[pop];
                }
                --nPopCode;
            }
            consts.push(stack[sp].re, stack[sp].im);
            op = VM.CONST;
        } else if (op == VM.CALL) {
            funcs.push(traceFuncs[0]);
        }
        code.push(op);
    }

    CompiledFunction getFun(int arity) {
        return new CompiledFunction(arity, code.toArray(), consts.getRe(), consts.getIm(), funcs.toArray());
    }
}
