package Filters;

/**
 * basic template for filter. Just need to change the coefficients *
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class Filter {

	protected double sampleRate;
	protected double centreFreq;
	protected double octaveBW;
	protected double gain;

	protected double bigA;
	protected double w0;
	protected double c;
	protected double s;
	protected double alpha;

	protected double a0;
	protected double a1;
	protected double a2;
	protected double b0;
	protected double b1;
	protected double b2;

	double xmem1, xmem2, ymem1, ymem2;

	/**
	 * Instantiates a new filter.
	 * 
	 * @param sampleRate
	 *            the sample rate
	 * @param centreFreq
	 *            the centre freq
	 * @param octaveBW
	 *            the octave bw
	 * @param gain
	 *            the gain
	 */
	public Filter(int sampleRate, double centreFreq, double octaveBW,
			double gain) {
		this.sampleRate = sampleRate;
		this.centreFreq = centreFreq;
		this.octaveBW = octaveBW;
		this.gain = gain;
		this.calculateMiddles();
		this.calculateCoefficients();
		this.reset();
	}

	/**
	 * Calculate coefficients.
	 */
	protected void calculateCoefficients() {
	}

	/**
	 * Calculate middles.
	 */
	protected void calculateMiddles() {
		this.bigA = Math.pow(10, gain / 20);
		this.w0 = 2 * Math.PI * this.centreFreq / this.sampleRate;
		this.c = Math.cos(w0);
		this.s = Math.sin(w0);
		this.alpha = s * Math.sinh(0.34657359028 * this.octaveBW * w0 / this.s);
	}

	/**
	 * Gets the centre freq.
	 * 
	 * @return the centre freq
	 */
	public double getCentreFreq() {
		return centreFreq;
	}

	/**
	 * Gets the gain.
	 * 
	 * @return the gain
	 */
	public double getGain() {
		return gain;
	}

	/**
	 * Gets the octave bw.
	 * 
	 * @return the octave bw
	 */
	public double getOctaveBW() {
		return octaveBW;
	}

	/**
	 * Gets the sample rate.
	 * 
	 * @return the sample rate
	 */
	public double getSampleRate() {
		return sampleRate;
	}

	/**
	 * Normalise coefficients.
	 */
	protected void normaliseCoefficients() {
		b0 /= a0;
		b1 /= a0;
		b2 /= a0;
		a1 /= a0;
		a2 /= a0;
	}

	/**
	 * Process.
	 * 
	 * @param x
	 *            the x
	 * @return the double
	 */
	public double process(double x) {
		double y = b0 * x + b1 * xmem1 + b2 * xmem2 - a1 * ymem1 - a2 * ymem2;
		xmem2 = xmem1;
		xmem1 = x;
		ymem2 = ymem1;
		ymem1 = y;
		return y;
	}

	/**
	 * Reset.
	 */
	public void reset() {
		xmem1 = xmem2 = ymem1 = ymem2 = 0;
	}

	/**
	 * Sets the centre freq.
	 * 
	 * @param centreFreq
	 *            the new centre freq
	 */
	public void setCentreFreq(double centreFreq) {
		this.centreFreq = centreFreq;
		this.calculateMiddles();
		this.calculateCoefficients();
		// this.reset();
	}

	/**
	 * Sets the gain.
	 * 
	 * @param gain
	 *            the new gain
	 */
	public void setGain(double gain) {
		this.gain = gain;
	}

	/**
	 * Sets the octave bw.
	 * 
	 * @param octaveBW
	 *            the new octave bw
	 */
	public void setOctaveBW(double octaveBW) {
		this.octaveBW = octaveBW;
	}

	/**
	 * Sets the sample rate.
	 * 
	 * @param sampleRate
	 *            the new sample rate
	 */
	public void setSampleRate(double sampleRate) {
		this.sampleRate = sampleRate;
	}

}
