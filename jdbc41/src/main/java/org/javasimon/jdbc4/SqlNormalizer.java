package org.javasimon.jdbc4;

/**
 * Interface that declares a normalizer: an object that accepts a query and returns a key that
 * can be used to treat same queries with different parameters as same query
 *
 * @author Anton Rybochkin
 * @since 4.1.4
 */
public interface SqlNormalizer {
	/**
	 * Get type of provided SQL query (SELECT, UPDATE, etc),  "batch" for batch queries
	 * @return SQL query type
	 */
	String getType();

	/**
	 * Get source SQL query
	 * @return the query
	 */
	String getSql();

	/**
	 * Get preprocessed query
	 * @return simon key
	 */
	String getNormalizedSql();
}
