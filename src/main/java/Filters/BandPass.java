package Filters;

/**
 * The Class BandPass.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class BandPass extends Filter {

	protected double q;

	/**
	 * Instantiates a new band pass.
	 * 
	 * @param sampleRate
	 *            the sample rate
	 * @param centreFreq
	 *            the centre freq
	 * @param octaveBW
	 *            the octave bw
	 */
	public BandPass(int sampleRate, double centreFreq, double octaveBW) {
		super(sampleRate, centreFreq, octaveBW, 0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.DSP.waveProcess.filters.Filter#calculateCoefficients()
	 */
	@Override
	protected void calculateCoefficients() {
		b0 = s / 2;
		b1 = 0;
		b2 = -s / 2;
		a0 = 1 + alpha;
		a1 = -2 * c;
		a2 = 1 - alpha;
		normaliseCoefficients();
	}

}
