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

/**
 * Contains static helper methods for formatting double values.
 */
public class Util {
    public static final int LEN_UNLIMITED = 100;
    public static final int FLOAT_PRECISION = -1;

    /**
     * Returns a number which is an approximation of v (within maxError)
     * and which has fewer digits in base-10).
     *
     * @param value    the value to be approximated
     * @param maxError the maximum deviation from value
     * @return an approximation with a more compact base-10 representation.
     */
    public static double shortApprox(double value, double maxError) {
        final double v = Math.abs(value);
        final double tail = MoreMath.intExp10(MoreMath.intLog10(Math.abs(maxError)));
        final double ret = Math.floor(v / tail + .5) * tail;
        return (value < 0) ? -ret : ret;
    }

    /**
     * Returns an approximation with no more than maxLen chars.
     * <p>
     * This method is not public, it is called through doubleToString,
     * that's why we can make some assumptions about the format of the string,
     * such as assuming that the exponent 'E' is upper-case.
     *
     * @param str    the value to truncate (e.g. "-2.898983455E20")
     * @param maxLen the maximum number of characters in the returned string
     * @return a truncation no longer then maxLen (e.g. "-2.8E20" for maxLen=7).
     */
    static String sizeTruncate(String str, int maxLen) {
        if (maxLen == LEN_UNLIMITED) {
            return str;
        }
        int ePos = str.lastIndexOf('E');
        String tail = (ePos != -1) ? str.substring(ePos) : "";
        int tailLen = tail.length();
        int headLen = str.length() - tailLen;
        int maxHeadLen = maxLen - tailLen;
        int keepLen = Math.min(headLen, maxHeadLen);
        if (keepLen < 1 || (keepLen < 2 && str.length() > 0 && str.charAt(0) == '-')) {
            return str; // impossible to truncate
        }
        int dotPos = str.indexOf('.');
        if (dotPos == -1) {
            dotPos = headLen;
        }
        if (dotPos > keepLen) {
            int exponent = (ePos != -1) ? Integer.parseInt(str.substring(ePos + 1)) : 0;
            int start = str.charAt(0) == '-' ? 1 : 0;
            exponent += dotPos - start - 1;
            String newStr = str.substring(0, start + 1) + '.' + str.substring(start + 1, headLen) + 'E' + exponent;
            return sizeTruncate(newStr, maxLen);

        }
        return str.substring(0, keepLen) + tail;
    }

    /**
     * Rounds by dropping roundingDigits of double precision
     * (similar to 'hidden precision digits' on calculators),
     * and formats to String.
     *
     * @param v              the value to be converted to String
     * @param roundingDigits the number of 'hidden precision' digits (e.g. 2).
     * @return a String representation of v
     */
    public static String doubleToString(final double v, final int roundingDigits) {
        final double absv = Math.abs(v);
        final String str = roundingDigits == FLOAT_PRECISION ? Float.toString((float) absv) : Double.toString(absv);
        StringBuffer buf = new StringBuffer(str);
        int roundingStart = (roundingDigits <= 0 || roundingDigits > 13) ? 17 : (16 - roundingDigits);

        int ePos = str.lastIndexOf('E');
        int exp = (ePos != -1) ? Integer.parseInt(str.substring(ePos + 1)) : 0;
        if (ePos != -1) {
            buf.setLength(ePos);
        }
        int len = buf.length();

        //remove dot
        int dotPos;
        for (dotPos = 0; dotPos < len && buf.charAt(dotPos) != '.'; ) {
            ++dotPos;
        }
        exp += dotPos;
        if (dotPos < len) {
            buf.deleteCharAt(dotPos);
            --len;
        }

        //round
        for (int p = 0; p < len && buf.charAt(p) == '0'; ++p) {
            ++roundingStart;
        }

        if (roundingStart < len) {
            if (buf.charAt(roundingStart) >= '5') {
                int p;
                for (p = roundingStart - 1; p >= 0 && buf.charAt(p) == '9'; --p) {
                    buf.setCharAt(p, '0');
                }
                if (p >= 0) {
                    buf.setCharAt(p, (char) (buf.charAt(p) + 1));
                } else {
                    buf.insert(0, '1');
                    ++roundingStart;
                    ++exp;
                }
            }
            buf.setLength(roundingStart);
        }

        //re-insert dot
        if ((exp < -5) || (exp > 10)) {
            buf.insert(1, '.');
            --exp;
        } else {
            for (int i = len; i < exp; ++i) {
                buf.append('0');
            }
            for (int i = exp; i <= 0; ++i) {
                buf.insert(0, '0');
            }
            buf.insert((exp <= 0) ? 1 : exp, '.');
            exp = 0;
        }
        len = buf.length();

        //remove trailing dot and 0s.
        int tail;
        for (tail = len - 1; tail >= 0 && buf.charAt(tail) == '0'; --tail) {
            buf.deleteCharAt(tail);
        }
        if (tail >= 0 && buf.charAt(tail) == '.') {
            buf.deleteCharAt(tail);
        }

        if (exp != 0) {
            buf.append('E').append(exp);
        }
        if (v < 0) {
            buf.insert(0, '-');
        }
        return buf.toString();
    }

    /**
     * Renders a real number to a String (for user display).
     *
     * @param maxLen   the maximum total length of the resulting string
     * @param rounding the number of final digits to round
     */
    public static String doubleToString(double x, int maxLen, int rounding) {
        return sizeTruncate(doubleToString(x, rounding), maxLen);
    }

    /**
     * Renders a complex number to a String (for user display).
     *
     * @param maxLen   the maximum total length of the resulting string
     * @param rounding the number of final digits to round
     */
    public static String complexToString(Complex x, int maxLen, int rounding) {
        //System.out.println("" + x.re + ' ' + x.im);
        if (x.im == 0) {
            return doubleToString(x.re, maxLen, rounding);
        }

        if (x.isNaN()) {
            return "NaN";
        }

        double xre = x.re, xim = x.im;
        if (x.isInfinite()) {
            if (!Double.isInfinite(xre)) {
                xre = 0;
            } else if (!Double.isInfinite(xim)) {
                xim = 0;
            }
        }
        if (xim == 0) {
            return doubleToString(xre, maxLen, rounding);
        }

        // insert plus between re & im
        boolean addPlus = xre != 0 && !(xim < 0);
        String sre = xre == 0 ? "" : doubleToString(xre, rounding);
        String sim = doubleToString(xim, rounding);
        String finalMultiply = Double.isInfinite(xim) ? "*" : "";

        if (sim.equals("1")) {
            sim = "";
        }
        if (sim.equals("-1")) {
            sim = "-";
        }

        if (maxLen != LEN_UNLIMITED) {
            --maxLen; // for final "i"
            if (addPlus) {
                --maxLen;
            }
            maxLen -= finalMultiply.length();

            int sreLen = sre.length();
            int simLen = sim.length();
            int reduce = sreLen + simLen - maxLen;
            if (reduce > 0) {
                int diff = Math.abs(sreLen - simLen);
                int rShort = reduce > diff ? (reduce - diff) / 2 : 0;
                int rLong = rShort + Math.min(reduce, diff);
                int sreTarget = sreLen;
                int simTarget = simLen;
                if (sreLen > simLen) {
                    sreTarget -= rLong;
                    simTarget -= rShort;
                } else {
                    sreTarget -= rShort;
                    simTarget -= rLong;
                }
                if (sreTarget + simTarget > maxLen) {
                    --simTarget;
                }
                sre = sizeTruncate(sre, sreTarget);
                sim = sizeTruncate(sim, simTarget);
            }
        }
        return sre + (addPlus ? "+" : "") + sim + finalMultiply + 'i';
    }
}
