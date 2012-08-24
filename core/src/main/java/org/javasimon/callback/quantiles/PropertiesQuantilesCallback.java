package org.javasimon.callback.quantiles;

import java.util.Properties;
import org.javasimon.Simon;
import org.javasimon.Stopwatch;
import org.javasimon.utils.SimonUtils;

/**
 * Callback which stores data in buckets to compute quantiles.
 * Buckets are created from configuration stored in a {@link java.util.Properties}
 * file. Configuration file should look like:
 * <pre>
 * # Global default values are set on root
 * .min=0
 * .max=60000
 * .nb=5
 * 
 * # For org.javasimon group: more buckets
 * org.javasimon.nb=10
 * 
 * # SlowClass is a performance bottleneck: higher upper bound
 * org.javasimon.slow.SlowClass.max=300000
 * </pre>
 * @author gquintana
 */
public class PropertiesQuantilesCallback extends QuantilesCallback {
	/**
	 * Properties containing configuration
	 */
	private final Properties properties;
	/**
	 * Main constructor
	 * @param properties Properties containing configuration
	 */
	public PropertiesQuantilesCallback(Properties properties) {
		this.properties = properties;
	}
	
	/**
	 * Create buckets using callback attributes
	 * @param stopwatch
	 * @return 
	 */
	@Override
	protected Buckets createBuckets(Stopwatch stopwatch) {
		Long min=getLongProperty(stopwatch,"min")*SimonUtils.NANOS_IN_MILLIS;
		Long max=getLongProperty(stopwatch,"max")*SimonUtils.NANOS_IN_MILLIS;
		Integer nb=getIntegerProperty(stopwatch,"nb");
		return createBuckets(stopwatch, min, max, nb);
	}
	/**
	 * Get property for Simon
	 * @param simon Simon
	 * @param name Property name
	 * @return Raw property value
	 */
	private String getProperty(Simon simon, String name) {
		return properties.getProperty(simon.getName()+"."+name);
	}

	private static String toString(String s) {
		if (s!=null) {
			s=s.trim();
			if (s.equals("")) {
				s=null;
			}
		}
		return s;
	}
	private static Long toLong(String s) {
		s=toString(s);
		Long l=null;
		if (s!=null) {
			try {
				l=Long.valueOf(s);
			} catch (NumberFormatException numberFormatException) {
				l=null;
			}
		}
		return l;
	}
	/**
	 * Get long property for Simon
	 * @param simon Simon
	 * @param name Property name
	 * @return Long property value
	 */
	private Long getLongProperty(Simon simon, String name) {
		Simon currentSimon=simon;
		Long l=null;
		while(l==null && currentSimon!=null) {
			l=toLong(getProperty(currentSimon, name));
			currentSimon = simon.getParent();
		}
		return l;
	}
	private static Integer toInteger(String s) {
		s=toString(s);
		Integer i=null;
		if (s!=null) {
			try {
				i=Integer.valueOf(s);
			} catch (NumberFormatException numberFormatException) {
				i=null;
			}
		}
		return i;
	}
	/**
	 * Get integer property for Simon
	 * @param simon Simon
	 * @param name Property name
	 * @return Long property value
	 */
	private Integer getIntegerProperty(Simon simon, String name) {
		Simon currentSimon=simon;
		Integer l=null;
		while(l==null && currentSimon!=null) {
			l=toInteger(getProperty(currentSimon, name));
			currentSimon = simon.getParent();
		}
		return l;
	}
}
