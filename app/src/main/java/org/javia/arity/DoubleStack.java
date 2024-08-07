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

class DoubleStack {
    private double re[] = new double[8];
    private double im[] = new double[8];
    private int size = 0;

    void clear() {
        size = 0;
    }

    void push(double a, double b) {
        if (size >= re.length) {
            int newSize = re.length << 1;
            double newRe[] = new double[newSize];
            double newIm[] = new double[newSize];
            System.arraycopy(re, 0, newRe, 0, re.length);
            System.arraycopy(im, 0, newIm, 0, re.length);
            re = newRe;
            im = newIm;
        }
        re[size] = a;
        im[size] = b;
        ++size;
    }

    void pop(int cnt) {
        if (cnt > size) {
            throw new Error("pop " + cnt + " from " + size);
        }
        size -= cnt;
    }

    void pop() {
        --size;
    }

    double[] getRe() {
        double trimmed[] = new double[size];
        System.arraycopy(re, 0, trimmed, 0, size);
        return trimmed;
    }

    double[] getIm() {
        boolean allZero = true;
        for (int i = 0; i < size; ++i) {
            if (im[i] != 0) {
                allZero = false;
                break;
            }
        }
        if (allZero) {
            return null;
        }
        double trimmed[] = new double[size];
        System.arraycopy(im, 0, trimmed, 0, size);
        return trimmed;
    }
}
