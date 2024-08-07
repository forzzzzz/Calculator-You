/*
 * Copyright (C) 2007-2008 Mihai Preda.
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

class Token {
    //kind
    static final int
            PREFIX = 1,
            LEFT = 2,
            RIGHT = 3,
            SUFIX = 4;

    final int priority;
    final int assoc;
    final int id;
    final byte vmop;

    double value;    //for NUMBER only
    String name = null; //for CONST & CALL
    int arity;
    int position;        //pos inside expression

    Token(int id, int priority, int assoc, int vmop) {
        this.id = id;
        this.priority = priority;
        this.assoc = assoc;
        this.vmop = (byte) vmop;
        arity = id == Lexer.CALL ? 1 : Symbol.CONST_ARITY;
    }

    Token setPos(int pos) {
        this.position = pos;
        return this;
    }

    Token setValue(double value) {
        this.value = value;
        return this;
    }

    Token setAlpha(String alpha) {
        name = alpha;
        return this;
    }

    public boolean isDerivative() {
        int len;
        return name != null && (len = name.length()) > 0 && name.charAt(len - 1) == '\'';
    }

    public String toString() {
        switch (id) {
            case Lexer.NUMBER:
                return "" + value;
            case Lexer.CALL:
                return name + '(' + arity + ')';
            case Lexer.CONST:
                return name;
        }
        return "" + id;
    }
}
