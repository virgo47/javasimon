package org.javasimon.jdbc;

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
		normalize();
	}

	private void normalize() {
		if (sql != null) {
			normalizedSql = sql.toLowerCase()
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
				.trim();
			type = normalizedSql.replaceAll("\\W*(\\w+)\\W.*", "$1");
		}
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
