/*
 * Copyright (C) 2008 Mihai Preda.
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

/**
 * Base class for functions.<p>
 * A function has an arity (the number of arguments), and a way for evaluation
 * given the values of the arguments.<p>
 * To create user-defined functions,
 * derive from this class and override one of the eval() methods.<p>
 * <p>
 * If the user subclasses Function, he is responsible for the thread-safety of
 * his user-defined Functions.
 */

abstract public class Function {
    String comment;
    private Function cachedDerivate = null;

    /**
     * Gives the arity of this function.
     *
     * @return the arity (the number of arguments). Arity >= 0.
     */
    abstract public int arity();

    public Function getDerivative() {
        if (cachedDerivate == null) {
            cachedDerivate = new Derivative(this);
        }
        return cachedDerivate;
    }

    void setDerivative(Function deriv) {
        cachedDerivate = deriv;
    }

    /**
     * Evaluates an arity-0 function (a function with no arguments).
     *
     * @return the value of the function
     */
    public double eval() {
        throw new ArityException(0);
    }

    /**
     * Evaluates a function with a single argument (arity == 1).
     */
    public double eval(double x) {
        throw new ArityException(1);
    }

    /**
     * Evaluates a function with two arguments (arity == 2).
     */
    public double eval(double x, double y) {
        throw new ArityException(2);
    }

    /**
     * Evaluates the function given the argument values.
     *
     * @param args array containing the arguments.
     * @return the value of the function
     * @throws ArityException if args.length != arity.
     */
    public double eval(double args[]) {
        switch (args.length) {
            case 0:
                return eval();
            case 1:
                return eval(args[0]);
            case 2:
                return eval(args[0], args[1]);
        }
        throw new ArityException(args.length);
    }


    /**
     * By default complex forwards to real eval is the arguments are real,
     * otherwise returns NaN.
     * This allow calling any real functions as a (restricted) complex one.
     */
    public Complex evalComplex() {
        checkArity(0);
        return new Complex(eval(), 0);
    }

    /**
     * Complex evaluates a function with a single argument.
     */
    public Complex eval(Complex x) {
        checkArity(1);
        return new Complex(x.im == 0 ? eval(x.re) : Double.NaN, 0);
    }

    /**
     * Complex evaluates a function with two arguments.
     */
    public Complex eval(Complex x, Complex y) {
        checkArity(2);
        return new Complex(x.im == 0 && y.im == 0 ? eval(x.re, y.re) : Double.NaN, 0);
    }

    /**
     * Complex evaluates a function with an arbitrary number of arguments.
     */
    public Complex eval(Complex args[]) {
        switch (args.length) {
            case 0:
                return evalComplex();
            case 1:
                return eval(args[0]);
            case 2:
                return eval(args[0], args[1]);
            default:
                int len = args.length;
                checkArity(len);
                double reArgs[] = new double[len];
                for (int i = args.length - 1; i >= 0; --i) {
                    if (args[i].im != 0) {
                        return new Complex(Double.NaN, 0);
                    }
                    reArgs[i] = args[i].re;
                }
                return new Complex(eval(reArgs), 0);
        }
    }

    public void checkArity(int nArgs) throws ArityException {
        if (arity() != nArgs) {
            throw new ArityException("Expected " + arity() + " arguments, got " + nArgs);
        }
    }

}
