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
	 * Create normalizer for batch query. Typically it should store "batch" in sql and type properties
	 * @param batch list of batch queries
	 * @return normalizer for batch queries
	 */
	SqlNormalizer getNormalizer(List<String> batch);
}
