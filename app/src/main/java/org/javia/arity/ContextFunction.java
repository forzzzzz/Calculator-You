package org.javia.arity;

public abstract class ContextFunction extends Function {
    private static final double NO_ARGS[] = new double[0];
    private static final Complex[] NO_ARGS_COMPLEX = new Complex[0];
    private static EvalContext context = new EvalContext();

    abstract public double eval(double args[], EvalContext context);

    abstract public Complex eval(Complex args[], EvalContext context);

    Complex[] toComplex(double args[], EvalContext context) {
        Complex argsC[];
        switch (args.length) {
            case 0:
                argsC = NO_ARGS_COMPLEX;
                break;
            case 1:
                argsC = context.args1c;
                argsC[0].set(args[0], 0);
                break;
            case 2:
                argsC = context.args2c;
                argsC[0].set(args[0], 0);
                argsC[1].set(args[1], 0);
                break;
            default:
                argsC = new Complex[args.length];
                for (int i = 0; i < args.length; ++i) {
                    argsC[i] = new Complex(args[i], 0);
                }
        }
        return argsC;
    }

    //@Override
    public double eval() {
        return eval(NO_ARGS);
    }

    //@Override
    public double eval(double x) {
        synchronized (context) {
            return eval(x, context);
        }
    }

    //@Override
    public double eval(double x, double y) {
        synchronized (context) {
            return eval(x, y, context);
        }
    }

    //@Override
    public double eval(double args[]) {
        synchronized (context) {
            return eval(args, context);
        }
    }

    public double eval(double x, EvalContext context) {
        double args[] = context.args1;
        args[0] = x;
        return eval(args, context);
    }

    public double eval(double x, double y, EvalContext context) {
        double args[] = context.args2;
        args[0] = x;
        args[1] = y;
        return eval(args, context);
    }

    //@Override
    public Complex evalComplex() {
        return eval(NO_ARGS_COMPLEX);
    }

    //@Override
    public Complex eval(Complex x) {
        synchronized (context) {
            return eval(x, context);
        }
    }

    //@Override
    public Complex eval(Complex x, Complex y) {
        synchronized (context) {
            return eval(x, y, context);
        }
    }

    //@Override
    public Complex eval(Complex args[]) {
        synchronized (context) {
            return eval(args, context);
        }
    }

    public Complex eval(Complex x, EvalContext context) {
        Complex args[] = context.args1c;
        args[0] = x;
        return eval(args, context);
    }

    public Complex eval(Complex x, Complex y, EvalContext context) {
        Complex args[] = context.args2c;
        args[0] = x;
        args[1] = y;
        return eval(args, context);
    }
}
