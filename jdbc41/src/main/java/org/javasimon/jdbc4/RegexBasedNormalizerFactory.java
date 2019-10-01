package org.javasimon.jdbc4;

import java.util.List;

public class RegexBasedNormalizerFactory implements SqlNormalizerFactory {
	@Override
	public SqlNormalizer getNormalizer(String sql) {
		return new RegexBasedSqlNormalizer(sql);
	}

	@Override
	public SqlNormalizer getNormalizer(List<String> batch) {
		return new RegexBasedSqlNormalizer(batch);
	}
}
