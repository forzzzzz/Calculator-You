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

import java.util.Vector;

class DeclarationParser extends TokenConsumer {
    static final String NO_ARGS[] = new String[0];
    static final int UNKNOWN_ARITY = -2;
    static final int MAX_ARITY = 5;

    String name;
    int arity;
    Vector args = new Vector();

    private SyntaxException exception;

    DeclarationParser(SyntaxException e) {
        this.exception = e;
    }

    //@Override
    void start() {
        name = null;
        arity = UNKNOWN_ARITY;
        args.setSize(0);
    }

    //@Override
    void push(Token token) throws SyntaxException {
        switch (token.id) {
            case Lexer.CALL:
                if (name == null) {
                    name = token.name;
                    arity = 0;
                } else {
                    throw exception.set("repeated CALL in declaration", token.position);
                }
                break;

            case Lexer.CONST:
                if (name == null) {
                    name = token.name;
                    arity = UNKNOWN_ARITY;
                } else if (arity >= 0) {
                    args.addElement(token.name);
                    ++arity;
                    if (arity > MAX_ARITY) {
                        throw exception.set("Arity too large " + arity, token.position);
                    }
                } else {
                    throw exception.set("Invalid declaration", token.position);
                }
                break;

            case Lexer.RPAREN:
            case Lexer.COMMA:
            case Lexer.END:
                break;

            default:
                throw exception.set("invalid token in declaration", token.position);
        }
    }

    String[] argNames() {
        if (arity > 0) {
            String argNames[] = new String[arity];
            args.copyInto(argNames);
            return argNames;
        } else {
            return NO_ARGS;
        }
    }
}
