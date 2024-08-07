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
 * Encapsulates together a function and its name.
 * Is used to return both the function and its name
 * from Compiler.compileWithName().
 */

public class FunctionAndName {
    public Function function;
    public String name;

    public FunctionAndName(Function fun, String name) {
        this.function = fun;
        this.name = name;
    }
}
