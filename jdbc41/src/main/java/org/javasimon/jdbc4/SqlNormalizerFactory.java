package org.javasimon.jdbc4;

import java.util.List;

/**
 * Factory to create objects that provide simon key by SQL query
 */
public interface SqlNormalizerFactory {
	/**
	 * Create normalizer for single SQL query
	 * @param sql query
	 * @return normalizer
	 */
	SqlNormalizer getNormalizer(String sql);

	/**
	 * Create normalizer for batch query. Typically it should
	 * @param batch
	 * @return
	 */
	SqlNormalizer getNormalizer(List<String> batch);
}
