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

/* Non-optimizing Code Generator
   Reads tokens in RPN (Reverse Polish Notation) order,
   and generates VM opcodes,
   without any optimization.
 */

class SimpleCodeGen extends TokenConsumer {
    static final SyntaxException HAS_ARGUMENTS = new SyntaxException();

    ByteStack code = new ByteStack();
    DoubleStack consts = new DoubleStack();
    FunctionStack funcs = new FunctionStack();

    //String argNames[];
    Symbols symbols;

    SyntaxException exception;

    SimpleCodeGen(SyntaxException exception) {
        this.exception = exception;
    }

    SimpleCodeGen setSymbols(Symbols symbols) {
        this.symbols = symbols;
        return this;
    }

    //@Override
    void start() {
        code.clear();
        consts.clear();
        funcs.clear();
    }

    Symbol getSymbol(Token token) throws SyntaxException {
        String name = token.name;
        boolean isDerivative = token.isDerivative();
        if (isDerivative) {
            if (token.arity == 1) {
                name = name.substring(0, name.length() - 1);
            } else {
                throw exception.set("Derivative expects arity 1 but found " + token.arity, token.position);
            }
        }
        Symbol symbol = symbols.lookup(name, token.arity);
        if (symbol == null) {
            throw exception.set("undefined '" + name + "' with arity " + token.arity, token.position);
        }
        if (isDerivative && symbol.op > 0 && symbol.fun == null) {
            symbol.fun = CompiledFunction.makeOpFunction(symbol.op);
        }
        if (isDerivative && symbol.fun == null) {
            throw exception.set("Invalid derivative " + name, token.position);
        }
        return symbol;
    }

    void push(Token token) throws SyntaxException {
        byte op;
        switch (token.id) {
            case Lexer.NUMBER:
                op = VM.CONST;
                consts.push(token.value, 0);
                break;

            case Lexer.CONST:
            case Lexer.CALL:
                Symbol symbol = getSymbol(token);
                if (token.isDerivative()) {
                    op = VM.CALL;
                    funcs.push(symbol.fun.getDerivative());
                } else if (symbol.op > 0) { // built-in
                    op = symbol.op;
                    if (op >= VM.LOAD0 && op <= VM.LOAD4) {
                        throw HAS_ARGUMENTS.set("eval() on implicit function", exception.position);
                    }
                } else if (symbol.fun != null) { // function call
                    op = VM.CALL;
                    funcs.push(symbol.fun);
                } else { // variable reference
                    op = VM.CONST;
                    consts.push(symbol.valueRe, symbol.valueIm);
                }
                break;

            default:
                op = token.vmop;
                if (op <= 0) {
                    throw new Error("wrong vmop: " + op + ", id " + token.id + " in \"" + exception.expression + '"');
                }
        }
        code.push(op);
    }

    CompiledFunction getFun() {
        return new CompiledFunction(0, code.toArray(), consts.getRe(), consts.getIm(), funcs.toArray());
    }

    /*
    double getValue() {
        return new CompiledFunction(0, code.toArray(), consts.getRe(), consts.getIm(), funcs.toArray()).eval();
    }
    */
}
