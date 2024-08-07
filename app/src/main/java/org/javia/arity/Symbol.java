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

public class Symbol {
    static final int CONST_ARITY = -3;
    byte op;
    Function fun;
    double valueRe, valueIm;
    boolean isConst = false;
    private String name;
    private int arity;

    private Symbol(String name, int arity, byte op, boolean isConst, int dummy) {
        setKey(name, arity);
        this.op = op;
        this.isConst = isConst;
    }

    Symbol(String name, Function fun) {
        setKey(name, fun.arity());
        this.fun = fun;
        // this.comment = fun.comment;
    }

    Symbol(String name, double re, boolean isConst) {
        this(name, re, 0, isConst);
    }

    Symbol(String name, double re, double im, boolean isConst) {
        setKey(name, CONST_ARITY);
        valueRe = re;
        valueIm = im;
        this.isConst = isConst;
    }

    static Symbol makeArg(String name, int order) {
        return new Symbol(name, CONST_ARITY, (byte) (VM.LOAD0 + order), false, 0);
    }

    static Symbol makeVmOp(String name, int op) {
        return new Symbol(name, (int) VM.arity[op], (byte) op, true, 0);
    }

    static Symbol newEmpty(Symbol s) {
        return new Symbol(s.name, s.arity, (byte) 0, false, 0);
    }

    public String toString() {
        return "Symbol '" + name + "' arity " + arity + " val " + valueRe + " op " + op;
    }

    /*
    public String getComment() {
	return comment;
    }
    */

    public String getName() {
        return name;
    }

    public int getArity() {
        return arity == CONST_ARITY ? 0 : arity;
    }

    boolean isEmpty() {
        return op == 0 && fun == null && valueRe == 0 && valueIm == 0;
    }

    Symbol setKey(String name, int arity) {
        this.name = name;
        this.arity = arity;
        return this;
    }

    public boolean equals(Object other) {
        Symbol symbol = (Symbol) other;
        return name.equals(symbol.name) && arity == symbol.arity;
    }

    public int hashCode() {
        return name.hashCode() + arity;
    }
}
