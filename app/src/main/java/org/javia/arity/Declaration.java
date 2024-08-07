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

class Declaration {
    private static final String NO_ARGS[] = {};
    String name;
    String args[];
    int arity;
    String expression;

    void parse(String source, Lexer lexer, DeclarationParser declParser) throws SyntaxException {
        int equalPos = source.indexOf('=');
        String decl;

        if (equalPos == -1) {
            decl = null;
            expression = source;
            name = null;
            args = NO_ARGS;
            arity = DeclarationParser.UNKNOWN_ARITY;
        } else {
            decl = source.substring(0, equalPos);
            expression = source.substring(equalPos + 1);
            lexer.scan(decl, declParser);
            name = declParser.name;
            args = declParser.argNames();
            arity = declParser.arity;
        }
        /*
        if (arity == DeclarationParser.UNKNOWN_ARITY) {
            args = IMPLICIT_ARGS;
        }
        */
    }
}
