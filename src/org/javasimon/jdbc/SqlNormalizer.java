package org.javasimon.jdbc;

import java.util.List;

/**
 * SqlNormalizer takes SQL statement and replaces parameters with question marks.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 17, 2008
 */
public class SqlNormalizer {
	private final String sql;
	private String normalizedSql;
	private String type;

	public SqlNormalizer(String sql) {
		this.sql = sql;
		if (sql != null) {
			normalizedSql = normalize(sql);
			type = getType(normalizedSql);
		}
	}

	public SqlNormalizer(List<String> batch) {
		type = "batch";
		sql = "batch";
		StringBuilder sqlBuilder = new StringBuilder();
		for (String statement : batch) {
			if (sqlBuilder.length() > 0) {
				sqlBuilder.append("; ");
			}
			sqlBuilder.append(normalize(statement));
		}
		this.normalizedSql = sqlBuilder.toString();
	}

	private String normalize(String sql) {
		return sql.toLowerCase()
			.replaceAll("''", "?") // replace empty strings and '' inside other strings
			.replaceAll(" *([=<>!,]+) *", "$1") // remove spaces around = and ,
			.replaceAll("\\s+", " ") // normalize white spaces
			.replaceAll("([(=<>!,]+)(?:(?:'[^']+')|(?:[0-9.]+))", "$1?") // replace arguments after =, ( and , with ?
			.replaceAll("([(=<>!,]+)\\w+\\([^)]*\\)", "$1?") // replace whole functions with ?
			.replaceAll("like '[^']+'", "like ?") // replace like arguments
			.replaceAll("between \\S+ and \\S+", "between ? and ?") // replace between arguments
			.replaceAll(" in\\(", " in (") // put space before ( in "in("
//					.replaceAll(" *\\(", "(") // remove spaces in front of (
			.replaceAll("([=<>!]+)", " $1 ") // put spaces around =, >=, <=, !=...
			.replaceAll(",", ", ") // put spaces after ,
			.replaceAll(" in \\(\\?(?:, \\?)*\\)", " in (?)") // shrink more ? in "in" to one
			.replaceAll("(create|alter|drop) (\\S+) ([^ (]+).*$", "$1 $2 $3") // shrink DDLs
			.replaceAll("^\\{|\\}$", "") // remove { and } at the start/end
			.trim();
	}

	private String getType(String sql) {
		return sql.replaceAll("\\W*(\\w+)\\W.*", "$1");
	}

	public String getSql() {
		return sql;
	}

	public String getNormalizedSql() {
		return normalizedSql;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "SqlNormalizer{" +
			"\n  sql='" + sql + '\'' +
			",\n  normalizedSql='" + normalizedSql + '\'' +
			",\n  type='" + type + '\'' +
			'}';
	}
}
