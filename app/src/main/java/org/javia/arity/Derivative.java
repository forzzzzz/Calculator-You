package org.javia.arity;

// f'(x)=Im(f(x+i*h)/h) 
public class Derivative extends Function {
    private static final double H = 1e-12;
    private static final double INVH = 1 / H;
    private final Function f;
    private Complex c = new Complex();

    public Derivative(Function f) throws ArityException {
        this.f = f;
        f.checkArity(1);
    }

    public double eval(double x) {
        return f.eval(c.set(x, H)).im * INVH;
    }

    public int arity() {
        return 1;
    }
}
