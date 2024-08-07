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

/**
 * A complex value, composed of the real part (re) and the imaginary part (im).
 * <p>
 * All the methods that return a Complex (such as add(), mul(), etc)
 * modify the object on which they are called and return it (this), in order
 * to avoid new object creation.
 */
public class Complex {
    /**
     * The real component.
     */
    public double re;

    /**
     * The imaginary component.
     */
    public double im;

    /**
     * Empty constructor, complex value 0.
     */
    public Complex() {
    }

    /**
     * Constructor taking the real and imaginary components.
     */
    public Complex(double re, double im) {
        set(re, im);
    }

    /**
     * Copy constructor.
     */
    public Complex(Complex o) {
        set(o);
    }

    /**
     * Sets the real and imaginary components.
     */
    public Complex set(double re, double im) {
        this.re = re;
        this.im = im;
        return this;
    }

    /**
     * Sets from other object (copy).
     */
    public Complex set(Complex o) {
        re = o.re;
        im = o.im;
        return this;
    }

    /**
     * Formats the real and imaginary part into a string.
     */
    public String toString() {
        return im == 0 ? "" + re : "(" + re + ", " + im + ')';
    }

    /**
     * Returns the real part if the imaginary part is zero, otherwise returns NaN.
     */
    public double asReal() {
        return im == 0 ? re : Double.NaN;
        //return Math.abs(im) < 1E-30 ? re : Double.NaN;
    }

    /**
     * Complex conjugate (negates imaginary).
     */
    public final Complex conjugate() {
        return set(re, -im);
    }

    /**
     * Negate, i.e. multiply with -1.
     */
    public final Complex negate() {
        return set(-re, -im);
    }

    /**
     * True if this is an infinite (and not a NaN).
     */
    public final boolean isInfinite() {
        return Double.isInfinite(re) || Double.isInfinite(im) && !isNaN();
    }

    /**
     * True if both the real and the imaginary parts
     * are finite (not infinite and not NaN).
     */
    public final boolean isFinite() {
        return !isInfinite() && !isNaN();
    }

    /**
     * True if either real or imaginary is NaN.
     */
    public final boolean isNaN() {
        return Double.isNaN(re) || Double.isNaN(im);
    }

    public final boolean equals(Complex o) {
        return ((re == o.re) || (re != re && o.re != o.re)) &&
                ((im == o.im) || (im != im && o.im != o.im));
    }

    /**
     * The argument (angle) in polar coordinates.
     */
    public final double arg() {
        return Math.atan2(im, re);
    }

    /**
     * The absolute value (length in polar coordinates).
     */
    public final double abs() {
        double a = Math.abs(re);
        double b = Math.abs(im);
        if (a == 0 || b == 0) {
            return a + b;
        }
        boolean aGreater = a > b;
        double q = aGreater ? b / a : a / b;
        return (aGreater ? a : b) * Math.sqrt(1 + q * q);
    }

    /**
     * The absolute value squared.
     * re^2 + im^2
     */
    public final double abs2() {
        return re * re + im * im;
    }

    /**
     * Addition.
     * Modifies and returns this.
     */
    public final Complex add(Complex o) {
        final double ulp = Math.ulp(re);
        re += o.re;
        im += o.im;
        // hack for "1.1-1-.1"
        if (Math.abs(re) < ulp * 1024) {
            re = 0;
        }
        return this;
    }

    /**
     * Substraction.
     */
    public final Complex sub(Complex o) {
        final double ulp = Math.ulp(re);
        re -= o.re;
        im -= o.im;
        // hack for "1.1-1-.1"
        if (Math.abs(re) < ulp * 1024) {
            re = 0;
        }
        return this;
    }

    Complex mul(double o) {
        re *= o;
        im *= o;
        return this;
    }

    /**
     * Multiplication.
     */
    public final Complex mul(Complex o) {
        double a = re, b = im, c = o.re, d = o.im;
        if (b == 0 && d == 0) {
            return set(a * c, 0);
        }

        final double mre = a * c - b * d;
        final double mim = a * d + b * c;

        if (!set(mre, mim).isNaN()) {
            return this;
        }

        if (set(a, b).isInfinite()) {
            normalizeInfinity();
            a = re;
            b = im;
        }

        if (o.isInfinite()) {
            set(c, d).normalizeInfinity();
            c = re;
            d = im;
        }

        if (b == 0) {
            if (d == 0) {
                return set(a * c, 0);
            }
            if (c == 0) {
                return set(0, a * d);
            }
            return set(a * c, a * d);
        }

        if (a == 0) {
            if (c == 0) {
                return set(-b * d, 0);
            }
            if (d == 0) {
                return set(0, b * c);
            }
            return set(-b * d, b * c);
        }

        if (d == 0) {
            return set(a * c, b * c);
        }
        if (c == 0) {
            return set(-b * d, a * d);
        }
        return set(mre, mim);
    }

    /**
     * Division.
     */
    public final Complex div(Complex o) {
        double c = o.re;
        double d = o.im;
        if (im == 0 && d == 0) {
            return set(re / c, 0);
        }
        if (o.isInfinite() && isFinite()) {
            return set(0, 0);
        }
        if (d == 0) {
            if (re == 0) {
                return set(0, im / c);
            }
            return set(re / c, im / c);
        }
        if (c == 0) {
            return set(im / d, -re / d);
        }
        if (Math.abs(c) > Math.abs(d)) {
            double q = d / c;
            double down = c + d * q;
            return set((re + im * q) / down, (im - re * q) / down);
        } else {
            double q = c / d;
            double down = c * q + d;
            return set((re * q + im) / down, (im * q - re) / down);
        }
    }

    /**
     * Complex square root.
     */
    public final Complex sqrt() {
        if (im == 0) {
            if (!(re < 0)) {
                set(Math.sqrt(re), 0);
            } else {
                set(0, Math.sqrt(-re));
            }
        } else {
            double t = Math.sqrt((Math.abs(re) + abs()) / 2);
            if (re >= 0) {
                set(t, im / (t + t));
            } else {
                set(Math.abs(im) / (t + t), im >= 0 ? t : -t);
            }
        }
        return this;
    }

    /**
     * Complex modulo (integer division remainder).
     */
    public final Complex mod(Complex o) {
        double a = re;
        double b = im;
        if (b == 0 && o.im == 0) {
            return set(a % o.re, 0);
        }
        return div(o).set(Math.rint(re), Math.rint(im)).mul(o).set(a - re, b - im);
    }

    /**
     * Complex GCD, Greatest Common Denominator.
     */
    public final Complex gcd(Complex o) {
        if (im == 0 && o.im == 0) {
            return set(MoreMath.gcd(re, o.re), 0);
        }
        Complex y = new Complex(o);
        double xabs2 = abs2();
        double yabs2 = y.abs2();
        while (xabs2 < yabs2 * 1e30) {
            double yRe = y.re;
            double yIm = y.im;
            y.set(mod(y));
            set(yRe, yIm);
            xabs2 = yabs2;
            yabs2 = y.abs2();
        }
        // normalize to positive & larger real
        if (Math.abs(re) < Math.abs(im)) {
            set(-im, re);
        }
        if (re < 0) {
            negate();
        }
        return this;
    }

    /**
     * Complex natural logarithm.
     */
    public final Complex log() {
        if (im == 0 && !(re < 0)) {
            return set(Math.log(re), 0);
        }
        double newIm = Math.atan2(im, re);
        return set(Math.log(abs()), newIm);
    }

    /**
     * Complex exponential.
     */
    public final Complex exp() {
        double expRe = Math.exp(re);
        if (im == 0) {
            return set(expRe, 0);
        } else {
            return set(expRe * MoreMath.cos(im), expRe * MoreMath.sin(im));
        }
    }

    /**
     * Complex square (x^2).
     */
    public final Complex square() {
        return set(re * re - im * im, 2 * re * im);
    }

    /**
     * Complex power (x^y == exp(y*log(x))).
     */
    public final Complex pow(Complex y) {
        if (y.im == 0) {
            if (y.re == 0) {
                // anything^0==1, including NaN^0 (!)
                return set(1, 0);
            }
            if (im == 0) {
                double res = Math.pow(re, y.re);
                if (res == res) { // !NaN
                    return set(res, 0);
                }
            }
            if (y.re == 2) {
                return square();
            }
            if (y.re == .5) {
                return sqrt();
            }
            double p = Math.pow(abs2(), y.re / 2);
            double a = arg() * y.re;
            return set(p * MoreMath.cos(a), p * MoreMath.sin(a));
        }
        if (im == 0 && re > 0) {
            double a = Math.pow(re, y.re);
            return set(0, y.im * Math.log(re)).exp().set(a * re, a * im);
        }
        return log().set(y.re * re - y.im * im, y.re * im + y.im * re).exp();
    }

    /**
     * Complex lgamma (log Gamma).
     */
    public final Complex lgamma() {
        double sumRe = 0.99999999999999709182, sumIm = 0;
        double down = re * re + im * im;
        double xplusk = re;
        double GAMMA[] = MoreMath.GAMMA;
        for (int k = 0; k < GAMMA.length; ++k) {
            ++xplusk;
            down += xplusk + xplusk - 1;
            double cc = GAMMA[k];
            sumRe += cc * xplusk / down;
            sumIm -= cc * im / down;
        }

        double a = re + .5;
        double tmpRe = re + 5.2421875;
        double saveIm = im;

        re = tmpRe;

        log();
        double termRe = a * re - saveIm * im + 0.9189385332046727418 - tmpRe;
        double termIm = a * im + saveIm * re - saveIm;

        set(sumRe, sumIm).log();
        re += termRe;
        im += termIm;
        return this;
    }

    /**
     * Complex factorial, based on lgamma().
     */
    public final Complex factorial() {
        return im == 0 ? set(MoreMath.factorial(re), 0) : lgamma().exp();
    }

    /**
     * sin(a+ib) = sin(a)*cosh(b) + i*cos(a)*sinh(b).
     */
    public final Complex sin() {
        return im == 0 ?
                set(MoreMath.sin(re), 0) :
                set(MoreMath.sin(re) * Math.cosh(im), MoreMath.cos(re) * Math.sinh(im));
    }

    /**
     * sinh(a+ib) = sinh(a)*cos(b) + i*cosh(a)*sin(b).
     */
    public final Complex sinh() {
        return im == 0 ? set(Math.sinh(re), 0) : swap().sin().swap();
    }

    /**
     * cos(a+ib) = cos(a)cosh(b) - i*sin(a)sinh(b).
     */
    public final Complex cos() {
        return im == 0 ?
                set(MoreMath.cos(re), 0) :
                set(MoreMath.cos(re) * Math.cosh(im), -MoreMath.sin(re) * Math.sinh(im));
    }

    /**
     * cosh(a+ib) = cosh(a)cos(b) + i*sinh(a)sin(b).
     */
    public final Complex cosh() {
        return im == 0 ? set(Math.cosh(re), 0) : swap().cos().conjugate();
    }

    /**
     * tan(a+ib) = sin(2a)/(cos(2a)+cosh(2b) + i*sinh(2b)/(cos(2a)+cosh(2b)).
     */
    public final Complex tan() {
        if (im == 0) {
            return set(MoreMath.tan(re), 0);
        }
        double aa = re + re;
        double bb = im + im;
        double down = MoreMath.cos(aa) + Math.cosh(bb);
        return set(MoreMath.sin(aa) / down, Math.sinh(bb) / down);
    }

    /**
     * tanh(a+ib) = sinh(2a)/(cosh(2a) + cos(2b)) + i*sin(2b)/(cosh(2a)+cos(2b)).
     */
    public final Complex tanh() {
        return im == 0 ? set(Math.tanh(re), 0) : swap().tan().swap();
    }

    /**
     * asin(x) = -i*log(sqrt(1-x^2)+ix).
     */
    public final Complex asin() {
        if (im == 0 && Math.abs(re) <= 1) {
            return set(Math.asin(re), 0);
        }
        double saveA = re;
        double saveB = im;
        return sqrt1z().set(re - saveB, im + saveA).log().set(im, -re);
    }

    /**
     * acos(x) = -i*log(x + i*sqrt(1-x^2)).
     */
    public final Complex acos() {
        if (im == 0 && Math.abs(re) <= 1) {
            return set(Math.acos(re), 0);
        }
        double saveA = re;
        double saveB = im;
        return sqrt1z().set(saveA - im, saveB + re).log().set(im, -re);
    }

    /**
     * atan(x) = i/2 * log((i+x)/(i-x)).
     */
    public final Complex atan() {
        if (im == 0) {
            return set(Math.atan(re), 0);
        }
        double a2 = re * re;
        double b2 = im * im;
        double down = a2 + b2 - im - im + 1;
        return set(-(a2 + b2 - 1) / down, -(re + re) / down).log().set(-im / 2, re / 2);
    }

    /**
     * asinh(x) = log(x+sqrt(x^2+1)).
     */
    public final Complex asinh() {
        if (im == 0) {
            return set(MoreMath.asinh(re), 0);
        }
        double a = re;
        double b = im;
        return set(re * re - im * im + 1, 2 * re * im).sqrt().set(re + a, im + b).log();
    }

    /**
     * acosh(x) = log(x+sqrt(x^2-1)).
     */
    public final Complex acosh() {
        if (im == 0 && re >= 1) {
            return set(MoreMath.acosh(re), 0);
        }
        double a = re;
        double b = im;
        return set(re * re - im * im - 1, 2 * re * im).sqrt().set(re + a, im + b).log();
    }

    /**
     * atanh(x) = log((1+x)/(1-x))/2.
     */
    public final Complex atanh() {
        if (im == 0) {
            return set(MoreMath.atanh(re), 0);
        }
        double a2 = re * re;
        double b2 = im * im;
        double down = a2 + 1 - re - re;
        return set((1 - a2 - b2) / down, (im + im) / down).log().set(re / 2, im / 2);
    }

    /**
     * Combinations: C(n, k) == exp(lgamma(n) - lgamma(k) - lgamma(n-k)).
     */
    public final Complex combinations(Complex o) {
        if (im == 0 && o.im == 0) {
            return set(MoreMath.combinations(re, o.re), 0);
        }

        double a = re;
        double b = im;

        lgamma();
        double c = re;
        double d = im;

        set(o).lgamma();
        double e = re;
        double f = im;

        set(a - o.re, b - o.im).lgamma();
        return set(c - e - re, d - f - im).exp();
    }

    /**
     * Permutations: P(n, k) == exp(lgamma(n) - lgamma(n-k)).
     */
    public final Complex permutations(Complex o) {
        if (im == 0 && o.im == 0) {
            return set(MoreMath.permutations(re, o.re), 0);
        }

        double a = re;
        double b = im;

        lgamma();
        double c = re;
        double d = im;

        set(a - o.re, b - o.im).lgamma();
        return set(c - re, d - im).exp();
    }

    /**
     * Swaps real and imaginary.
     */
    private final Complex swap() {
        return set(im, re);
    }

    /**
     * Normalizes the finite components of an infinity to zero.
     * Used internally by mul().
     * Assumes this is infinite.
     */
    private final Complex normalizeInfinity() {
        // assumes this.isInfinite()
        if (!Double.isInfinite(im)) {
            im = 0;
        } else if (!Double.isInfinite(re)) {
            re = 0;
        }
        return this;
    }

    /**
     * sqrt(1-x^2)
     */
    private final Complex sqrt1z() {
        return set(1 - re * re + im * im, -2 * re * im).sqrt();
    }
}
