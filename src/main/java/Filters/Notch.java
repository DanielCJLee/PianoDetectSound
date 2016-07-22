package Filters;

public class Notch extends Filter {

	protected double q;

	public Notch(int sampleRate, double centreFreq, double strength) {
		super(sampleRate, centreFreq, strength, 0);

	}

	@Override
	protected void calculateCoefficients() {
		b0 = 1;
		b1 = -2 * c;
		b2 = 1;
		a0 = 1 + alpha;
		a1 = -2 * c;
		a2 = 1 - alpha;
		normaliseCoefficients();
	}

}
