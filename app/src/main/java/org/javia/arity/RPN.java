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

import java.util.Stack;

/* Reverse Polish Notation
   reads tokens in normal infix order (e.g.: 1 + 2)
   and outputs them in Reverse Polish order (e.g.: 1 2 +).
   See Dijkstra's Shunting Yard algorithm: 
   http://en.wikipedia.org/wiki/Shunting_yard_algorithm
 */
class RPN extends TokenConsumer {
    Stack stack = new Stack();
    int prevTokenId = 0;
    TokenConsumer consumer;
    SyntaxException exception;

    RPN(SyntaxException exception) {
        this.exception = exception;
    }

    static final boolean isOperand(int id) {
        return
                id == Lexer.FACT ||
                        id == Lexer.RPAREN ||
                        id == Lexer.NUMBER ||
                        id == Lexer.CONST ||
                        id == Lexer.PERCENT;
    }

    void setConsumer(TokenConsumer consumer) {
        this.consumer = consumer;
    }

    //@Override
    void start() {
        stack.removeAllElements();
        prevTokenId = 0;
        consumer.start();
    }

    private Token top() {
        return stack.empty() ? null : (Token) stack.peek();
    }

    private void popHigher(int priority) throws SyntaxException {
        Token t = top();
        while (t != null && t.priority >= priority) {
            consumer.push(t);
            //code.push(t);
            stack.pop();
            t = top();
        }
    }

    void push(Token token) throws SyntaxException {
        int priority = token.priority;
        int id = token.id;
        switch (id) {
            case Lexer.NUMBER:
            case Lexer.CONST:
                if (isOperand(prevTokenId)) {
                    push(Lexer.TOK_MUL);
                }
                consumer.push(token);
                break;

            case Lexer.RPAREN: {
                if (prevTokenId == Lexer.CALL) {
                    top().arity--;
                } else if (!isOperand(prevTokenId)) {
                    throw exception.set("unexpected ) or END", token.position);
                }

                popHigher(priority);
                Token t = top();
                if (t != null) {
                    if (t.id == Lexer.CALL) {
                        consumer.push(t);
                    } else if (t != Lexer.TOK_LPAREN) {
                        throw exception.set("expected LPAREN or CALL", token.position);
                    }
                    stack.pop();
                }
                break;
            }

            case Lexer.COMMA: {
                if (!isOperand(prevTokenId)) {
                    throw exception.set("misplaced COMMA", token.position);
                }
                popHigher(priority);
                Token t = top();
                if (t == null || t.id != Lexer.CALL) {
                    throw exception.set("COMMA not inside CALL", token.position);
                }
                t.arity++;
                //code.push(stack.pop());
                break;
            }

            case Lexer.END: {
                Token t = Lexer.TOK_RPAREN;
                t.position = token.position;
                do {
                    push(t);
                } while (top() != null);
                break;
            }

            default: //operators, CALL, LPAREN
                if (token.assoc == Token.PREFIX) {
                    if (isOperand(prevTokenId)) {
                        push(Lexer.TOK_MUL);
                    }
                    stack.push(token);
                    break;
                }
                if (!isOperand(prevTokenId)) {
                    if (id == Lexer.SUB) {
                        //change SUB to unary minus
                        token = Lexer.TOK_UMIN;
                        stack.push(token);
                        break;
                    } else if (id == Lexer.ADD) {
                        // ignore, keep prevTokenId unchanged
                        return;
                    }
                    throw exception.set("operator without operand", token.position);
                }
                popHigher(priority + (token.assoc == Token.RIGHT ? 1 : 0));
                stack.push(token);
        }
        prevTokenId = token.id;
    }
}
