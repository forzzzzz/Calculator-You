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
 * A constant presented as a function, always evaluates to the same value.
 */
public class Constant extends Function {
    private Complex value;

    public Constant(Complex o) {
        value = new Complex(o);
    }

    //@Override

    /**
     * Returns the complex constant.
     */
    public Complex evalComplex() {
        return value;
    }

    //@Override

    /**
     * Returns the complex constant as a real value.
     *
     * @see Complex.asReas()
     */
    public double eval() {
        return value.asReal();
    }

    public String toString() {
        return value.toString();
    }

    public int arity() {
        return 0;
    }
}
