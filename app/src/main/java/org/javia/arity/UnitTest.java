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

class EvalCase {
    static final double ERR = -2, FUN = -3;
    String expr;
    double result;
    Complex cResult;

    EvalCase(String expr, double result) {
        this.expr = expr;
        this.result = result;
    }

    EvalCase(String expr, Complex result) {
        this.expr = expr;
        this.cResult = result;
    }
}

class TestEval {
    private static final double ONE_SQRT2 = 0.7071067811865475; // sin(pi/4)
    static EvalCase cases[] = {
            new EvalCase(".", 0),
            new EvalCase("1+.", 1),
            new EvalCase("1", 1),
            new EvalCase("\u03c0", Math.PI),
            new EvalCase("2\u00d73", 6), //2*3
            new EvalCase("1+\u221a9*2", 7), //1+sqrt(9)*2
            new EvalCase("3\u221a 4", 6), //3*sqrt(4)
            new EvalCase("\u221a16sin(2\u03c0/4)", 4), //sqrt(16)*sin(2pi/4)
            new EvalCase("1+", EvalCase.ERR),
            new EvalCase("1+1", 2),
            new EvalCase("1+-1", 0),
            new EvalCase("-0.5", -.5),
            new EvalCase("+1e2", 100),
            new EvalCase("1e-1", .1),
            new EvalCase("1e\u22122", .01), //unicode minus
            new EvalCase("-2^3!", -64),
            new EvalCase("(-2)^3!", 64),
            new EvalCase("-2^1^2", -2),
            new EvalCase("--1", 1),
            new EvalCase("-3^--2", -9),
            new EvalCase("1+2)(2+3", 15),
            new EvalCase("1+2)!^-2", 1. / 36),
            new EvalCase("sin(0)", 0),
            new EvalCase("cos(0)", 1),
            new EvalCase("sin(-1--1)", 0),
            new EvalCase("-(2+1)*-(4/2)", 6),
            new EvalCase("-.5E-1", -.05),
            new EvalCase("1E1.5", EvalCase.ERR),
            new EvalCase("2 3 4", 24),
            new EvalCase("pi", Math.PI),
            new EvalCase("e", Math.E),
            new EvalCase("sin(pi/2)", 1),
            new EvalCase("f=sin(2x)", EvalCase.FUN),
            new EvalCase("f(pi/2)", 0),
            new EvalCase("a=3", 3),
            new EvalCase("b=a+1", 4),
            new EvalCase("f(x, y) = x*(y+1)", EvalCase.FUN),
            new EvalCase("=", EvalCase.ERR),
            new EvalCase("f(a, b-a)", 6),
            new EvalCase(" f(a pi/4)", -1),
            new EvalCase("f (  1  +  1  , a+1)", 10),
            new EvalCase("g(foo) = f (f(foo, 1)pi/2)", EvalCase.FUN),
            new EvalCase("g(.5*2)", 0),
            new EvalCase("NaN", Double.NaN),
            new EvalCase("Inf", Double.POSITIVE_INFINITY),
            new EvalCase("Infinity", Double.POSITIVE_INFINITY),
            new EvalCase("-Inf", Double.NEGATIVE_INFINITY),
            new EvalCase("0/0", Double.NaN),

            new EvalCase("comb(11, 9)", 55),
            new EvalCase("perm(11, 2)", 110),
            new EvalCase("comb(1000, 999)", 1000),
            new EvalCase("perm(1000, 1)", 1000),

            new EvalCase("c(x)=1+x^2", EvalCase.FUN),
            new EvalCase("c(3-1)", 5),
            new EvalCase("abs(3-4i)", 5),
            new EvalCase("exp(pi*i)", -1),

            new EvalCase("5%", 0.05),
            new EvalCase("200+5%", 210),
            new EvalCase("200-5%", 190),
            new EvalCase("100/200%", 50),
            new EvalCase("100+200%+5%", 315),
            new EvalCase("p1(x)=200+5%+x", EvalCase.FUN),
            new EvalCase("p1(0)", 210),
            new EvalCase("p2(x,y)=x+y%+(2*y)%", EvalCase.FUN),
            new EvalCase("p2(200,5)", 231),

            new EvalCase("mod(5,3)", 2),
            new EvalCase("5.2 # 3.2", 2),

            new EvalCase("f(x)=3", EvalCase.FUN),
            new EvalCase("g(x)=f(x)", EvalCase.FUN),
            new EvalCase("g(1)", 3),

            new EvalCase("a(x)=i+x-x", EvalCase.FUN),
            new EvalCase("b(x)=a(x)*a(x)", EvalCase.FUN),
            new EvalCase("b(5)", -1),

            new EvalCase("h(x)=sqrt(-1+x-x)", EvalCase.FUN),
            new EvalCase("k(x)=h(x)*h(x)", EvalCase.FUN),
            new EvalCase("k(5)", -1),

            new EvalCase("pi=4", 4),
            new EvalCase("pi", Math.PI),

            new EvalCase("fc(x)=e^(i*x^2", EvalCase.FUN),
            new EvalCase("fc(0)", 1),
            new EvalCase("aa(x)=sin(x)^1+sin(x)^0", EvalCase.FUN),
            new EvalCase("aa(0)", 1),
            new EvalCase("null(x)=0", EvalCase.FUN),
            new EvalCase("n(x)=null(sin(x))", EvalCase.FUN),
            new EvalCase("n(1)", 0),
            new EvalCase("(2,", EvalCase.ERR),

            new EvalCase("100.1-100-.1", 0),
            new EvalCase("1.1-1+(-.1)", 0),

            new EvalCase("log(2,8)", 3),
            new EvalCase("log(9,81)", 2),
            new EvalCase("log(4,2)", .5),

            new EvalCase("sin'(0)", 1),
            new EvalCase("cos'(0)", 0),
            new EvalCase("cos'(pi/2)", -1),
            new EvalCase("f(x)=2*x^3+x^2+100", EvalCase.FUN),
            new EvalCase("f'(1)", 8),
            new EvalCase("f'(2)", 28),
            new EvalCase("abs'(2)", 1),
            new EvalCase("abs'(-3)", -1),

            new EvalCase("0x0", 0),
            new EvalCase("0x100", 256),
            new EvalCase("0X10", 16),
            new EvalCase("0b10", 2),
            new EvalCase("0o10", 8),
            new EvalCase("0o8", EvalCase.ERR),
            new EvalCase("0xg", EvalCase.ERR),
            new EvalCase("0b20", EvalCase.ERR),
            new EvalCase("sin(0x1*pi/2)", 1),

            new EvalCase("ln(e)", 1),
            new EvalCase("log(10)", 1),
            new EvalCase("log10(100)", 2),
            new EvalCase("lg(.1)", -1),
            new EvalCase("log2(2)", 1),
            new EvalCase("lb(256)", 8),

            new EvalCase("rnd()*0", 0),
            new EvalCase("rnd(5)*0", 0),

            new EvalCase("max(2,3)", 3),
            new EvalCase("min(2,3)", 2),
            new EvalCase("fm(x)=max(2, x)", EvalCase.FUN),
            new EvalCase("fm(6)", 6),
            new EvalCase("fmin(x)=min(2, x)", EvalCase.FUN),
            new EvalCase("fmin(1)", 1),
            new EvalCase("fmin(3)", 2),
            new EvalCase("cbrt(8)", 2),
            new EvalCase("cbrt(-8)", -2),

            new EvalCase("s=sign(x)", EvalCase.FUN),
            new EvalCase("s(2)", 1),
            new EvalCase("s(-2)", -1),
            new EvalCase("s(0)", 0),
            new EvalCase("s(nan)", Double.NaN),

            new EvalCase("real(8.123)", 8.123),
            new EvalCase("imag(8.123)", 0),
            new EvalCase("im(sqrt(-1))", 1),
            new EvalCase("im(nan)", Double.NaN),
    };
    static EvalCase casesComplex[] = {
            new EvalCase("sqrt(-1)^2", new Complex(-1, 0)),
            new EvalCase("i", new Complex(0, 1)),
            new EvalCase("sqrt(-1)", new Complex(0, 1)),

            new EvalCase("c(2+0i)", new Complex(5, 0)),
            new EvalCase("c(1+i)", new Complex(1, 2)),
            new EvalCase("ln(-1)", new Complex(0, -Math.PI)),
            new EvalCase("i^i", new Complex(0.20787957635076193, 0)),
            new EvalCase("gcd(135-14i, 155+34i)", new Complex(12, -5)),
            new EvalCase("comb(1+.5i, 1)", new Complex(1, .5)),
            new EvalCase("perm(2+i, 2)", new Complex(1, 3)),
            new EvalCase("fc(2)", new Complex(-0.6536436208636119, -0.7568024953079282)),

            new EvalCase("sign(2i)", new Complex(0, 1)),
            new EvalCase("sign(-i)", new Complex(0, -1)),
            new EvalCase("sign(nan)", new Complex(Double.NaN, 0)),
            new EvalCase("sign(nan i)", new Complex(Double.NaN, 0)),
            new EvalCase("sign(0)", new Complex(0, 0)),
            new EvalCase("sign(2-2i)", new Complex(ONE_SQRT2, -ONE_SQRT2)),

            // These functions have no imaginary part
            new EvalCase("real(8.123)", new Complex(8.123, 0)),
            new EvalCase("imag(8.123)", new Complex(0, 0)),
            new EvalCase("real(1+3i)", new Complex(1, 0)),
            new EvalCase("imag(1+3i)", new Complex(3, 0)),
            new EvalCase("re(1+3i)", new Complex(1, 0)),
            new EvalCase("im(1+3i)", new Complex(3, 0)),
    };

    static boolean testEval() throws ArityException {
        final String spaces = "                                           ";
        boolean allOk = true;
        Symbols symbols = new Symbols();
        for (int i = 0; i < cases.length; ++i) {
            EvalCase c = cases[i];
            String strResult;
            boolean ok = true;
            try {
                double actual, actual2 = 0;
                Complex complex, complex2 = new Complex();

                FunctionAndName fan = symbols.compileWithName(c.expr);
                Function f = fan.function;
                symbols.define(fan);
                if (f.arity() == 0) {
                    actual = f.eval();
                    complex = f.evalComplex();
                    ok = ok && UnitTest.equal(actual, complex);
                    strResult = Util.doubleToString(actual, 1);

                    if (!Symbols.isDefinition(c.expr)) {
                        actual2 = symbols.eval(c.expr);
                        complex2 = symbols.evalComplex(c.expr);
                        ok = ok && UnitTest.equal(actual, actual2) && complex.equals(complex2);
                    }

                    if (!ok) {
                        System.out.println("**** failed: " + actual + ' ' + actual2 + ' ' + complex + ' ' + complex2);
                    }
                } else {
                    actual = EvalCase.FUN;
                    strResult = f.toString();
                }
                ok = ok && UnitTest.equal(c.result, actual);
            } catch (SyntaxException e) {
                strResult = e.toString();
                ok = c.result == EvalCase.ERR;
            }
            System.out.println((ok ? "" : "failed (expected " + c.result + "): ")
                    + c.expr
                    + spaces.substring(0, Math.max(15 - c.expr.length(), 0)) + " = "
                    + strResult);
            if (!ok) {
                allOk = false;
            }
        }

        for (int i = 0; i < casesComplex.length; ++i) {
            try {
                EvalCase c = casesComplex[i];
                Complex result = symbols.evalComplex(c.expr);
                if (!UnitTest.equal(c.cResult, result)) {
                    System.out.println("failed " + c.expr + " expected " + c.cResult +
                            " got " + result);
                    allOk = false;
                } else {
                    System.out.println("" + c.expr + " = " + Util.complexToString(result, 40, 0));
                }
            } catch (SyntaxException e) {
            }
        }
        return allOk;
    }
}


class FormatCase {
    public int rounding;
    public double val;
    public String res;
    public FormatCase(int rounding, double v, String s) {
        this.rounding = rounding;
        this.val = v;
        this.res = s;
    }
}

class SizeCase {
    public int size;
    public String val;
    public String res;
    public SizeCase(int size, String v, String s) {
        this.size = size;
        this.val = v;
        this.res = s;
    }
}

class TestFormat {
    static FormatCase cases[] = {
            new FormatCase(0, 0.1, "0.1"),
            new FormatCase(0, 0.12, "0.12"),
            new FormatCase(0, 0.001, "0.001"),
            new FormatCase(0, 0.0012, "0.0012"),
            new FormatCase(0, 0.0000001, "1E-7"),
            new FormatCase(0, 0.00000012, "1.2E-7"),
            new FormatCase(0, 0.123456789012345, "0.123456789012345"),

            new FormatCase(0, 0, "0"),
            new FormatCase(0, 1, "1"),
            new FormatCase(0, 12, "12"),
            new FormatCase(0, 1234567890., "1234567890"),
            new FormatCase(0, 1000000000., "1000000000"),

            new FormatCase(0, 1.23456789012345, "1.23456789012345"),
            new FormatCase(0, 12345.6789012345, "12345.6789012345"),
            new FormatCase(0, 1234567890.12345, "1234567890.12345"),
            new FormatCase(0, 123456789012345., "1.23456789012345E14"),
            new FormatCase(0, 100000000000000., "1E14"),
            new FormatCase(0, 120000000000000., "1.2E14"),
            new FormatCase(0, 100000000000001., "1.00000000000001E14"),

            new FormatCase(2, 0.1, "0.1"),
            new FormatCase(2, 0.00000012, "1.2E-7"),
            new FormatCase(2, 0.123456789012345, "0.12345678901235"),

            new FormatCase(2, 0, "0"),

            new FormatCase(2, 1.23456789012345, "1.2345678901235"),
            new FormatCase(3, 1.23456789012345, "1.234567890123"),

            new FormatCase(0, 12345.6789012345, "12345.6789012345"),
            new FormatCase(2, 1234567890.12345, "1234567890.1235"),
            new FormatCase(3, 123456789012345., "1.234567890123E14"),
            new FormatCase(2, 100000000000001., "1E14"),

            new FormatCase(0, 12345678901234567., "1.2345678901234568E16"),
            new FormatCase(2, 12345678901234567., "1.2345678901235E16"),

            new FormatCase(0, 99999999999999999., "1E17"),
            new FormatCase(0, 9999999999999999., "1E16"),
            new FormatCase(0, 999999999999999., "9.99999999999999E14"),
            new FormatCase(2, 999999999999999., "1E15"),
            new FormatCase(2, 999999999999994., "9.9999999999999E14"),

            new FormatCase(2, MoreMath.log2(1 + .00002), "0.000028853612282487"),
            new FormatCase(0, 4E-4, "0.0004"),
            new FormatCase(0, 1e30, "1E30"),
    };
    static SizeCase[] sizeCases = {
            new SizeCase(9, "1111111110", "1.11111E9"),
            new SizeCase(10, "1111111110", "1111111110"),
            new SizeCase(10, "11111111110", "1.11111E10"),
            new SizeCase(10, "12.11111E9", "12.11111E9"),
            new SizeCase(9, "12.34567E9", "12.3456E9"),
            new SizeCase(9, "12345678E3", "1.2345E10"),
            new SizeCase(9, "-12345678E3", "-1.234E10"),

            new SizeCase(9, "-0.00000007", "-0.000000"),

            new SizeCase(5, "-1.23E123", "-1.23E123"),
            new SizeCase(5, "-1.2E123", "-1.2E123"),
            new SizeCase(5, "-1E123", "-1E123"),
            new SizeCase(2, "-1", "-1"),
            new SizeCase(1, "-1", "-1"),
            new SizeCase(1, "-0.02", "-0.02"),
            new SizeCase(1, "0.02", "0"),
    };

    static boolean testFormat() {
        boolean ret = true;
        for (int i = 0; i < cases.length; ++i) {
            FormatCase c = cases[i];
            double v = Double.parseDouble(c.res);
            if (c.rounding == 0 && v != c.val) {
                System.out.println("wrong test? " + c.res + " " + v + " " + c.val);
            }
            String res = Util.doubleToString(c.val, c.rounding);
            if (!res.equals(c.res)) {
                System.out.println("Expected '" + c.res + "', got '" + res + "'. " + Double.toString(c.val));
                ret = false;
            }
            int nKeep = c.rounding == 0 ? 17 : 15 - c.rounding;
            //System.out.println("" + Double.toString(c.val) + " " + Util.round(c.val, nKeep) + " " + c.res + ", got " + res);
        }
        return ret;
    }

    static boolean testSizeCases() {
        boolean ret = true;
        for (SizeCase c : sizeCases) {
            String truncated = Util.sizeTruncate(c.val, c.size);
            if (!truncated.equals(c.res)) {
                System.out.println("sizeTruncate(" + c.val + ", " + c.size + "): got '" + truncated +
                        "' expected '" + c.res + "'");
                ret = false;
            }
        }
        return ret;
    }
}

/**
 * Runs unit-tests.<p>
 * Usage: java -jar arity.jar
 */
public class UnitTest {
    private static final String profileCases[] = {
            //"1+1",
            "(100.5 + 20009.999)*(7+4+3)/(5/2)^3!)*2",
            "fun1(x)=(x+2)*(x+3)",
            "otherFun(x)=(fun1(x-1)*x+1)*(fun1(2-x)+10)",
            "log(x+30.5, 3)^.7*sin(x+.5)"
    };
    static boolean allOk = true;
    static int checkCounter = 0;

    /**
     * Takes a single command-line argument, an expression; compiles and prints it.<p>
     * Without arguments, runs the unit tests.
     *
     * @throws SyntaxException if there are errors compiling the expression.
     */
    public static void main(String argv[]) throws SyntaxException, ArityException {
        int size = argv.length;
        if (size == 0) {
            runUnitTests();
            //profile();
        } else if (argv[0].equals("-profile")) {
            if (size == 1) {
                profile();
            } else {
                Symbols symbols = new Symbols();
                for (int i = 1; i < size - 1; ++i) {
                    FunctionAndName fan = symbols.compileWithName(argv[i]);
                    symbols.define(fan);
                }
                profile(symbols, argv[size - 1]);
            }
        } else {
            Symbols symbols = new Symbols();
            for (int i = 0; i < size; ++i) {
                FunctionAndName fan = symbols.compileWithName(argv[i]);
                symbols.define(fan);
                Function f = fan.function;
                System.out.println(argv[i] + " : " + f);
            }
        }
    }

    static void profile(Symbols symbols, String str) throws SyntaxException, ArityException {
        Function f = symbols.compile(str);
        System.out.println("\n" + str + ": " + f);

        Runtime runtime = Runtime.getRuntime();

        runtime.gc();
        /*
        long m1 = runtime.freeMemory();
        for (int i = 0; i < 200; ++i) {
            symbols.compile(str);
        }
        long m2 = runtime.freeMemory();
        System.out.println("compilation memory: " + (m1 - m2)/200 + " bytes");
        */

        runtime.gc();
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 1000; ++i) {
            symbols.compile(str);
        }
        long t2 = System.currentTimeMillis();
        System.out.println("compilation time: " + (t2 - t1) + " us");

        double args[] = new double[f.arity()];
        /*
        runtime.gc();
        m1 = runtime.freeMemory();
        f.eval(args);
        m2 = runtime.freeMemory();
        if (m2 != m1) {
            System.out.println("execution memory: " + (m1 - m2) + " bytes");
        }
        */

        runtime.gc();
        t1 = System.currentTimeMillis();
        for (int i = 0; i < 100000; ++i) {
            f.eval(args);
        }
        t2 = System.currentTimeMillis();
        long delta = t2 - t1;
        System.out.println("execution time: " + (delta > 100 ? "" + delta / 100. + " us" : "" + delta + " ns"));
    }

    private static void profile() {
        String cases[] = profileCases;
        Symbols symbols = new Symbols();
        try {
            for (int i = 0; i < cases.length; ++i) {
                symbols.define(symbols.compileWithName(cases[i]));
                profile(symbols, cases[i]);
            }
        } catch (SyntaxException e) {
            throw new Error("" + e);
        }
    }

    static void runUnitTests() {
        checkCounter = 0;

        check(Util.doubleToString(Double.NEGATIVE_INFINITY, 5).equals("-Infinity"));
        check(Util.doubleToString(Double.NaN, 5).equals("NaN"));

        Complex c = new Complex();
        Complex d = new Complex();
        Complex e = new Complex();

        check(Util.complexToString(c.set(0, -1), 10, 1).equals("-i"));
        check(Util.complexToString(c.set(2.123, 0), 3, 0).equals("2.1"));
        check(Util.complexToString(c.set(0, 1.0000000000001), 20, 3).equals("i"));
        check(Util.complexToString(c.set(1, -1), 10, 1).equals("1-i"));
        check(Util.complexToString(c.set(1, 1), 10, 1).equals("1+i"));
        check(Util.complexToString(c.set(1.12, 1.12), 9, 0).equals("1.12+1.1i"));
        check(Util.complexToString(c.set(1.12345, -1), 7, 0).equals("1.123-i"));

        check(c.set(-1, 0).pow(d.set(0, 1)), e.set(0.04321391826377226, 0));
        check(c.set(-1, 0).pow(d.set(1, 1)), e.set(-0.04321391826377226, 0));

        check(c.set(-1, 0).abs(), 1);
        check(c.set(Math.E * Math.E, 0).log(), d.set(2, 0));
        check(c.set(-1, 0).log(), d.set(0, Math.PI));

        check(c.set(2, 0).exp(), d.set(Math.E * Math.E, 0));
        check(c.set(0, Math.PI).exp(), d.set(-1, 0));

        check(MoreMath.lgamma(1), 0);
        check(c.set(1, 0).lgamma(), d.set(0, 0));

        check(c.set(0, 0).factorial(), d.set(1, 0));
        check(c.set(1, 0).factorial(), d.set(1, 0));
        check(c.set(0, 1).factorial(), d.set(0.49801566811835596, -0.1549498283018106));
        check(c.set(-2, 1).factorial(), d.set(-0.17153291990834815, 0.32648274821006623));
        check(c.set(4, 0).factorial(), d.set(24, 0));
        check(c.set(4, 3).factorial(), d.set(0.016041882741649555, -9.433293289755953));

        check(Math.log(-1), Double.NaN);
        check(Math.log(-0.03), Double.NaN);
        check(MoreMath.intLog10(-0.03), 0);
        check(MoreMath.intLog10(0.03), -2);
        check(MoreMath.intExp10(3), 1000);
        check(MoreMath.intExp10(-1), 0.1);

        check(Util.shortApprox(1.235, 0.02), 1.24);
        check(Util.shortApprox(1.235, 0.4), 1.2000000000000002);
        check(Util.shortApprox(-1.235, 0.02), -1.24);
        check(Util.shortApprox(-1.235, 0.4), -1.2000000000000002);

        check(TestFormat.testFormat());

        check(TestEval.testEval());

        check(testRecursiveEval());

        check(testFrame());

        check(TestFormat.testSizeCases());

        if (!allOk) {
            System.out.println("\n*** Some tests FAILED ***\n");
            System.exit(1);
        } else {
            System.out.println("\n*** All tests passed OK ***\n");
        }
    }

    static boolean testFrame() {
        boolean ok = true;
        try {
            Symbols symbols = new Symbols();
            symbols.define("a", 1);
            ok = ok && symbols.eval("a") == 1;

            symbols.pushFrame();
            ok = ok && symbols.eval("a") == 1;
            symbols.define("a", 2);
            ok = ok && symbols.eval("a") == 2;
            symbols.define("a", 3);
            ok = ok && symbols.eval("a") == 3;

            symbols.popFrame();
            ok = ok && symbols.eval("a") == 1;

            // ----

            symbols = new Symbols();
            symbols.pushFrame();
            symbols.add(Symbol.makeArg("base", 0));
            symbols.add(Symbol.makeArg("x", 1));
            ok = ok && symbols.lookupConst("x").op == VM.LOAD1;
            symbols.pushFrame();
            ok = ok && symbols.lookupConst("base").op == VM.LOAD0;
            ok = ok && symbols.lookupConst("x").op == VM.LOAD1;
            symbols.popFrame();
            ok = ok && symbols.lookupConst("base").op == VM.LOAD0;
            ok = ok && symbols.lookupConst("x").op == VM.LOAD1;
            symbols.popFrame();
            ok = ok && symbols.lookupConst("x").op == VM.LOAD0;
        } catch (SyntaxException e) {
            return false;
        }
        return ok;
    }

    static boolean equal(Complex a, Complex b) {
        return equal(a.re, b.re) && equal(a.im, b.im);
    }

    static boolean equal(double a, Complex c) {
        return equal(a, c.re)
                && (equal(0, c.im) ||
                Double.isNaN(a) && Double.isNaN(c.im));
    }

    static boolean equal(double a, double b) {
        return a == b ||
                (Double.isNaN(a) && Double.isNaN(b)) ||
                Math.abs((a - b) / b) < 1E-15 || Math.abs(a - b) < 1E-15;
    }

    static void check(double v1, double v2) {
        ++checkCounter;
        if (!equal(v1, v2)) {
            allOk = false;
            System.out.println("failed check #" + checkCounter + ": expected " + v2 + " got " + v1);
        }
    }

    static void check(Complex v1, Complex v2) {
        ++checkCounter;
        if (!(equal(v1.re, v2.re) && equal(v1.im, v2.im))) {
            allOk = false;
            System.out.println("failed check #" + checkCounter + ": expected " + v2 + " got " + v1);
        }
    }

    static void check(boolean cond) {
        ++checkCounter;
        if (!cond) {
            allOk = false;
            //Log.log("check " + checkCounter + " failed");
        }
    }

    static boolean testRecursiveEval() {
        Symbols symbols = new Symbols();
        symbols.define("myfun", new MyFun());
        try {
            Function f = symbols.compile("1+myfun(x)");
            return
                    f.eval(0) == 2 &&
                            f.eval(1) == 1 &&
                            f.eval(2) == 0 &&
                            f.eval(3) == -1;
        } catch (SyntaxException e) {
            System.out.println("" + e);
            allOk = false;
            return false;
        }
    }
}

class MyFun extends Function {
    Symbols symbols = new Symbols();
    Function f;

    MyFun() {
        try {
            f = symbols.compile("1-x");
        } catch (SyntaxException e) {
            System.out.println("" + e);
        }
    }

    public double eval(double x) {
        return f.eval(x);
    }

    public int arity() {
        return 1;
    }
}
