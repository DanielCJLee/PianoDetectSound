package Process;


import Utils.Complex;
//import plotting.FFTController;
//import riff.Signal;
//import util.ArrayMethods;

/**
 * Created by HuuPhuoc on 12/14/15.
 */
public class FFT {


    double[] amplitudes;
    private int frameSize = 0;
//    public Signal signal;
//    private FFTBox fftBox;

    // compute the FFT of x[], assuming its length is a power of 2
    public static Complex[] fft(Complex[] x) {
        int N = x.length;

        // base case
        if (N == 1) return new Complex[]{x[0]};

        // radix 2 Cooley-Tukey FFT
        if (N % 2 != 0) {
            throw new RuntimeException("N is not a power of 2");
        }

        // fft of even terms
        Complex[] even = new Complex[N / 2];
        for (int k = 0; k < N / 2; k++) {
            even[k] = x[2 * k];
        }
        Complex[] q = fft(even);

        // fft of odd terms
        Complex[] odd = even;  // reuse the array
        for (int k = 0; k < N / 2; k++) {
            odd[k] = x[2 * k + 1];
        }
        Complex[] r = fft(odd);

        // combine
        Complex[] y = new Complex[N];
        for (int k = 0; k < N / 2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].plus(wk.times(r[k]));
            y[k + N / 2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }


    // compute the inverse FFT of x[], assuming its length is a power of 2
    public static Complex[] ifft(Complex[] x) {
        int N = x.length;
        Complex[] y = new Complex[N];

        // take conjugate
        for (int i = 0; i < N; i++) {
            y[i] = x[i].conjugate();
        }

        // compute forward FFT
        y = fft(y);

        // take conjugate again
        for (int i = 0; i < N; i++) {
            y[i] = y[i].conjugate();
        }

        // divide by N
        for (int i = 0; i < N; i++) {
            y[i] = y[i].times(1.0 / N);
        }

        return y;

    }

    // compute the circular convolution of x and y
    public static Complex[] cconvolve(Complex[] x, Complex[] y) {

        // should probably pad x and y with 0s so that they have same length
        // and are powers of 2
        if (x.length != y.length) {
            throw new RuntimeException("Dimensions don't agree");
        }

        int N = x.length;

        // compute FFT of each sequence
        Complex[] a = fft(x);
        Complex[] b = fft(y);

        // point-wise multiply
        Complex[] c = new Complex[N];
        for (int i = 0; i < N; i++) {
            c[i] = a[i].times(b[i]);
        }

        // compute inverse FFT
        return ifft(c);
    }


    // compute the linear convolution of x and y
    public static Complex[] convolve(Complex[] x, Complex[] y) {
        Complex ZERO = new Complex(0, 0);

        Complex[] a = new Complex[2 * x.length];
        for (int i = 0; i < x.length; i++) a[i] = x[i];
        for (int i = x.length; i < 2 * x.length; i++) a[i] = ZERO;

        Complex[] b = new Complex[2 * y.length];
        for (int i = 0; i < y.length; i++) b[i] = y[i];
        for (int i = y.length; i < 2 * y.length; i++) b[i] = ZERO;

        return cconvolve(a, b);
    }

    // display an array of Complex numbers to standard output
    public static void show(Complex[] x, String title) {
        System.out.println(title);
        System.out.println("-------------------");
        for (int i = 0; i < x.length; i++) {
            System.out.println(x[i]);
        }
        System.out.println();
    }


    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        Complex[] x = new Complex[N];

        // original data
        for (int i = 0; i < N; i++) {
            x[i] = new Complex(i, 0);
            x[i] = new Complex(-2 * Math.random() + 1, 0);
        }
        show(x, "x");

        // FFT of original data
        Complex[] y = fft(x);
        show(y, "y = fft(x)");

        // take inverse FFT
        Complex[] z = ifft(y);
        show(z, "z = ifft(y)");

        // circular convolution of x with itself
        Complex[] c = cconvolve(x, x);
        show(c, "c = cconvolve(x, x)");

        // linear convolution of x with itself
        Complex[] d = convolve(x, x);
        show(d, "d = convolve(x, x)");
    }

    /**
     * the cfft algorithm *.
     *
     * @param amplitudes the amplitudes
     * @return the complex[]
     */
    protected static Complex[] cfft(Complex[] amplitudes) {
        double bigN = amplitudes.length;
        // Log.d("BN" + bigN);
        if (bigN <= 1) { // base case
            return amplitudes;
        }
        double halfN = bigN / 2;
        Complex[] evens = new Complex[(int) halfN];
        Complex[] odds = new Complex[(int) halfN];
        // this doesn't work for odd values,
        // as this is happening recursively
        // I assume this is why frame has to be a
        // value of 2...
        // divide
        for (int i = 0; i < halfN; ++i) {
            evens[i] = amplitudes[i * 2];
            odds[i] = amplitudes[i * 2 + 1];
        }
        // conquer
        evens = cfft(evens);
        odds = cfft(odds);
        double a = -2 * Math.PI; //
        // combine
        for (int i = 0; i < halfN; ++i) {
            double p = i / bigN;
            Complex t = new Complex(0, a * p).exp();
            t = t.mul(odds[i]); // could be wrong way around...
            amplitudes[i] = t.add(evens[i]); // same
            amplitudes[(int) (i + halfN)] = evens[i].sub(t);
        }
        return amplitudes;
    }

    /**
     * return the frequency row based on sample rate and frame size *.
     *
     * @param s         the s
     * @param frameSize the frame size
     * @return the freq row
     */
//    public static double[] getFreqRow(Signal s, double frameSize) {
//        double sr = s.getSampleRate() / frameSize;
//        double[] fr = new double[(int) frameSize];
//        for (int i = 0; i < frameSize; ++i) {
//            fr[i] = i * sr;
//        }
//        return fr;
//    }

    /**
     * Invert fft algorithm *.
     *
     * @param amplitudes the amplitudes
     * @return the complex[]
     */
    public static Complex[] icfft(Complex[] amplitudes) {
        double n = amplitudes.length;
        // conjugate...
        for (int i = 0; i < n; ++i) {
            amplitudes[i].re *= -1;
        }
        // apply transform
        amplitudes = cfft(amplitudes);
        for (int i = 0; i < n; ++i) {
            // conjugate again
            amplitudes[i].im *= -1;
            // scale
            amplitudes[i].re /= n;
            amplitudes[i].im /= n;
        }
        return amplitudes;
    }

    /**
     * Normalise fft amplitudes to 1/N *.
     *
     * @param amplitudes the amplitudes
     * @return the double[]
     */
    public static double[] normalise(Complex[] amplitudes) {
        double l = amplitudes.length;
        double[] na = new double[amplitudes.length];
        for (int i = 0; i < amplitudes.length; ++i) {
            na[i] = amplitudes[i].re / l;
        }
        return na;
    }

    /**
     * Normalise fft amplitudes to 1/N *.
     *
     * @param amplitudes the amplitudes
     * @return the double[]
     */
    public static double[] normalise(double[] amplitudes) {
        double l = amplitudes.length;
        double[] na = new double[amplitudes.length];
        for (int i = 0; i < amplitudes.length; ++i) {
            na[i] = amplitudes[i] / l;
        }
        return na;
    }

    protected Complex[] cValues; // complex values of FFT (unnormalised)




}
