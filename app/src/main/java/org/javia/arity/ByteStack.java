/*
 * Copyright (C) 2008-2009 Mihai Preda.
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

class ByteStack {
    private byte[] data = new byte[8];
    private int size = 0;

    void clear() {
        size = 0;
    }

    void push(byte b) {
        if (size >= data.length) {
            byte[] newData = new byte[data.length << 1];
            System.arraycopy(data, 0, newData, 0, data.length);
            data = newData;
        }
        data[size++] = b;
    }

    /*
    void pop(int cnt) {
        size -= cnt;
    }
    */

    byte pop() {
        return data[--size];
    }

    byte[] toArray() {
        byte[] trimmed = new byte[size];
        System.arraycopy(data, 0, trimmed, 0, size);
        return trimmed;
    }
}
