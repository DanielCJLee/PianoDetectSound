package Utils;

/**
 * Created by HuuPhuoc on 12/8/15.
 */
public class Complex {
    public static final Complex I = new Complex(0.0D, 1.0D);
    public static final Complex ZERO = new Complex(0.0D);
    public static final Complex ONE = new Complex(1.0D);
    public static final Complex TWO = new Complex(2.0D);
    public static final Complex NaN = new Complex(0.0D / 0.0, 0.0D / 0.0);
    public static final Complex INF = new Complex(1.0D / 0.0, 1.0D / 0.0);
    public double re;
    public double im;

    public Complex(double var1, double var3) {
        this.re = var1;
        this.im = var3;
    }

    public Complex(double var1) {
        this(var1, 0.0D);
    }

    public double re() {
        return this.re;
    }

    public double im() {
        return this.im;
    }

    public double toDouble(double var1) {
        double var3 = Math.abs(this.im);
        if (var3 > var1 && var3 > Math.abs(this.re) * var1) {
            throw new RuntimeException("The imaginary part of the complex number is not neglectable small for the conversion to a real number. re=" + this.re + " im=" + this.im + " eps=" + var1 + ".");
        } else {
            return this.re;
        }
    }
    /**
     * return all magnitudes (distance) of a complex array *.
     *
     * @param values
     *            the values
     * @return the magnitudes
     */
    public static double[] getMagnitudes(Complex[] values) {
        double[] cs = new double[values.length];
        for (int i = 0; i < values.length; ++i) {
            cs[i] = values[i].getDistance();
        }
        return cs;
    }

    /**
     * return all reals of a complex array *.
     *
     * @param values
     *            the values
     * @return the reals
     */
    public static double[] getReals(Complex[] values) {
        double[] cs = new double[values.length];
        for (int i = 0; i < values.length; ++i) {
            cs[i] = values[i].re;
        }
        return cs;
    }
    public double getDistance() {
        return Math.pow((this.im * this.im) + (this.re * this.re), 0.5);
    }
    public static Complex[] doubleToComplex(double[] values) {
        Complex[] cs = new Complex[values.length];
        for (int i = 0; i < values.length; ++i) {
            cs[i] = new Complex(values[i]);
        }
        return cs;
    }
    public boolean isNaN() {
        return Double.isNaN(this.re) || Double.isNaN(this.im);
    }

    public boolean isInfinite() {
        return Double.isInfinite(this.re) || Double.isInfinite(this.im);
    }

    public static Complex expj(double var0) {
        return new Complex(Math.cos(var0), Math.sin(var0));
    }

    public static Complex fromPolar(double var0, double var2) {
        return new Complex(var0 * Math.cos(var2), var0 * Math.sin(var2));
    }

    public double abs() {
        return Math.hypot(this.re, this.im);
    }

    public double arg() {
        return Math.atan2(this.im, this.re);
    }

    public Complex conj() {
        return new Complex(this.re, -this.im);
    }

    public Complex neg() {
        return new Complex(-this.re, -this.im);
    }

    public Complex reciprocal() {
        if (this.isNaN()) {
            return NaN;
        } else if (this.isInfinite()) {
            return new Complex(0.0D, 0.0D);
        } else {
            double var1 = this.re * this.re + this.im * this.im;
            return var1 == 0.0D ? INF : new Complex(this.re / var1, -this.im / var1);
        }
    }

    public Complex exp() {
        return fromPolar(Math.exp(this.re), this.im);
    }

    public Complex log() {
        return new Complex(Math.log(this.abs()), this.arg());
    }

    public Complex sqr() {
        return new Complex(this.re * this.re - this.im * this.im, 2.0D * this.re * this.im);
    }

    public Complex sqrt() {
        if (this.re == 0.0D && this.im == 0.0D) {
            return new Complex(0.0D, 0.0D);
        } else {
            double var1 = this.abs();
            return new Complex(Math.sqrt((var1 + this.re) / 2.0D), Math.copySign(1.0D, this.im) * Math.sqrt((var1 - this.re) / 2.0D));
        }
    }

    public Complex add(double var1) {
        return new Complex(this.re + var1, this.im);
    }

    public Complex add(Complex var1) {
        return new Complex(this.re + var1.re, this.im + var1.im);
    }

    public Complex sub(double var1) {
        return new Complex(this.re - var1, this.im);
    }

    public Complex sub(Complex var1) {
        return new Complex(this.re - var1.re, this.im - var1.im);
    }

    public static Complex sub(double var0, Complex var2) {
        return new Complex(var0 - var2.re, -var2.im);
    }

    public Complex mul(double var1) {
        return new Complex(this.re * var1, this.im * var1);
    }

    public Complex mul(Complex var1) {
        return new Complex(this.re * var1.re - this.im * var1.im, this.re * var1.im + this.im * var1.re);
    }

    public Complex div(double var1) {
        return new Complex(this.re / var1, this.im / var1);
    }

    public Complex div(Complex var1) {
        double var2 = var1.re * var1.re + var1.im * var1.im;
        return new Complex((this.re * var1.re + this.im * var1.im) / var2, (this.im * var1.re - this.re * var1.im) / var2);
    }

    public static Complex div(double var0, Complex var2) {
        double var3 = var2.re * var2.re + var2.im * var2.im;
        return new Complex(var0 * var2.re / var3, -var0 * var2.im / var3);
    }

    public Complex pow(int var1) {
        return fromPolar(Math.pow(this.abs(), (double) var1), this.arg() * (double) var1);
    }

    public Complex pow(double var1) {
        return this.log().mul(var1).exp();
    }

    public Complex pow(Complex var1) {
        return this.log().mul(var1).exp();
    }

    public boolean equals(Complex var1, double var2) {
        return Math.abs(this.re() - var1.re()) <= var2 && Math.abs(this.im() - var1.im()) <= var2;
    }
    public double getArg() {
        return Math.atan(this.im / this.re);
    }
    /**
     * e to the power of this *.
     *
     * @return the complex
     */

    public Complex plus(Complex b) {
        Complex a = this;             // invoking object
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    // return a new Complex object whose value is (this - b)
    public Complex minus(Complex b) {
        Complex a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new Complex(real, imag);
    }

    // return a new Complex object whose value is (this * b)
    public Complex times(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    // scalar multiplication
    // return a new object whose value is (this * alpha)
    public Complex times(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    // return a new Complex object whose value is the conjugate of this
    public Complex conjugate() {  return new Complex(re, -im); }

    // return a new Complex object whose value is the reciprocal of this


    // return the real or imaginary part
    public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        } else if (!(var1 instanceof Complex)) {
            return false;
        } else {
            Complex var2 = (Complex) var1;
            return this.re == var2.re && this.im == var2.im;
        }
    }

    public int hashCode() {
        long var1 = Double.doubleToLongBits(this.re);
        long var3 = Double.doubleToLongBits(this.im);
        return (int) (var1 ^ var1 >>> 32 ^ var3 ^ var3 >>> 32);
    }

    public double getReal() {
        return 2;
    }

    public String toString() {
        return "(" + this.re + ", " + this.im + ")";
    }

}
