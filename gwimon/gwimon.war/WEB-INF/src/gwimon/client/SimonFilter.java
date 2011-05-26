package gwimon.client;

import java.io.Serializable;

/**
 * Filter object that specifies what should be returned in {@link SimonAggregation}, can narrow down Simon list,
 * or even trigger reset on Simons (works as a command, sort of, too)
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public class SimonFilter implements Serializable {
	/** Regex mask for Simons. */
	private String mask;

	/** Filters out {@link org.javasimon.UnknownSimon} and unused ones (counter 0). */
	boolean undef;

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public boolean isUndef() {
		return undef;
	}

	public void setUndef(boolean undef) {
		this.undef = undef;
	}
}
