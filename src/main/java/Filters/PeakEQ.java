package Filters;

/**
 * The Class PeakEQ.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class PeakEQ extends Filter {

	/**
	 * Instantiates a new peak eq.
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
	public PeakEQ(int sampleRate, double centreFreq, double octaveBW,
			double gain) {
		super(sampleRate, centreFreq, octaveBW, gain);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.DSP.waveProcess.filters.Filter#calculateCoefficients()
	 */
	@Override
	protected void calculateCoefficients() {
		b0 = 1 + alpha * bigA;
		b1 = -2 * c;
		b2 = 1 - alpha * bigA;
		a0 = 1 + alpha / bigA;
		a1 = -2 * c;
		a2 = 1 - alpha / bigA;
		normaliseCoefficients();
	}

}
