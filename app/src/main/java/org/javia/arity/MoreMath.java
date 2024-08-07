/*
 * Copyright (C) 2006-2009 Mihai Preda.
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
 *
 */

package org.javia.arity;

class MoreMath {
    static final double GAMMA[] = {
            57.156235665862923517,
            -59.597960355475491248,
            14.136097974741747174,
            -0.49191381609762019978,
            .33994649984811888699e-4,
            .46523628927048575665e-4,
            -.98374475304879564677e-4,
            .15808870322491248884e-3,
            -.21026444172410488319e-3,
            .21743961811521264320e-3,
            -.16431810653676389022e-3,
            .84418223983852743293e-4,
            -.26190838401581408670e-4,
            .36899182659531622704e-5
    };
    static final double FACT[] = {
            1.0,
            40320.0,
            2.0922789888E13,
            6.204484017332394E23,
            2.631308369336935E35,
            8.159152832478977E47,
            1.2413915592536073E61,
            7.109985878048635E74,
            1.2688693218588417E89,
            6.1234458376886085E103,
            7.156945704626381E118,
            1.8548264225739844E134,
            9.916779348709496E149,
            1.0299016745145628E166,
            1.974506857221074E182,
            6.689502913449127E198,
            3.856204823625804E215,
            3.659042881952549E232,
            5.5502938327393044E249,
            1.3113358856834524E267,
            4.7147236359920616E284,
            2.5260757449731984E302,
    };
    private static final double LOG2E = 1.4426950408889634074;

    public static final double asinh(double x) {
        return (x < 0) ? -asinh(-x) : Math.log(x + x + 1 / (Math.sqrt(x * x + 1) + x));
    }

    public static final double acosh(double x) {
        return Math.log(x + x - 1 / (Math.sqrt(x * x - 1) + x));
    }

    public static final double atanh(double x) {
        return (x < 0) ? -atanh(-x) : 0.5 * Math.log(1. + (x + x) / (1 - x));
    }

    public static final double trunc(double x) {
        return x >= 0 ? Math.floor(x) : Math.ceil(x);
    }

    public static final double gcd(double x, double y) {
        if (Double.isNaN(x) || Double.isNaN(y) ||
                Double.isInfinite(x) || Double.isInfinite(y)) {
            return Double.NaN;
        }
        x = Math.abs(x);
        y = Math.abs(y);
        while (x < y * 1e15) {
            final double save = y;
            y = x % y;
            x = save;
        }
        return x;
    }

    public static final double lgamma(double x) {
        double tmp = x + 5.2421875; //== 607/128. + .5;
        double sum = 0.99999999999999709182;
        for (int i = 0; i < GAMMA.length; ++i) {
            sum += GAMMA[i] / ++x;
        }

        return 0.9189385332046727418 //LN_SQRT2PI, ln(sqrt(2*pi))
                + Math.log(sum)
                + (tmp - 4.7421875) * Math.log(tmp) - tmp
                ;
    }

    public static final double factorial(double x) {
        if (x < 0) { // x <= -1 ?
            return Double.NaN;
        }
        if (x <= 170) {
            if (Math.floor(x) == x) {
                int n = (int) x;
                double extra = x;
                switch (n & 7) {
                    case 7:
                        extra *= --x;
                    case 6:
                        extra *= --x;
                    case 5:
                        extra *= --x;
                    case 4:
                        extra *= --x;
                    case 3:
                        extra *= --x;
                    case 2:
                        extra *= --x;
                    case 1:
                        return FACT[n >> 3] * extra;
                    case 0:
                        return FACT[n >> 3];
                }
            }
        }
        return Math.exp(lgamma(x));
    }

    public static final double combinations(double n, double k) {
        if (n < 0 || k < 0) {
            return Double.NaN;
        }
        if (n < k) {
            return 0;
        }
        if (Math.floor(n) == n && Math.floor(k) == k) {
            k = Math.min(k, n - k);
            if (n <= 170 && 12 < k && k <= 170) {
                return factorial(n) / factorial(k) / factorial(n - k);
            } else {
                double r = 1, diff = n - k;
                for (double i = k; i > .5 && r < Double.POSITIVE_INFINITY; --i) {
                    r *= (diff + i) / i;
                }
                return r;
            }
        } else {
            return Math.exp(lgamma(n) - lgamma(k) - lgamma(n - k));
        }
    }

    public static final double permutations(double n, double k) {
        if (n < 0 || k < 0) {
            return Double.NaN;
        }
        if (n < k) {
            return 0;
        }
        if (Math.floor(n) == n && Math.floor(k) == k) {
            if (n <= 170 && 10 < k && k <= 170) {
                return factorial(n) / factorial(n - k);
            } else {
                double r = 1, limit = n - k + .5;
                for (double i = n; i > limit && r < Double.POSITIVE_INFINITY; --i) {
                    r *= i;
                }
                return r;
            }
        } else {
            return Math.exp(lgamma(n) - lgamma(n - k));
        }
    }

    public static final double log2(double x) {
        return Math.log(x) * LOG2E;
    }

    private static final boolean isPiMultiple(double x) {
        // x % y == 0
        final double d = x / Math.PI;
        return d == Math.floor(d);
    }

    public static final double sin(double x) {
        return isPiMultiple(x) ? 0 : Math.sin(x);
    }

    public static final double cos(double x) {
        return isPiMultiple(x - Math.PI / 2) ? 0 : Math.cos(x);
    }

    public static final double tan(double x) {
        return isPiMultiple(x) ? 0 : Math.tan(x);
    }

    public static final int intLog10(double x) {
        //an alternative implem is using a for loop.
        return (int) Math.floor(Math.log10(x));
        //return (int)log10(x);
    }

    public static final double intExp10(int exp) {
        return Double.parseDouble("1E" + exp);
    }
}
