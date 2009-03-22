package org.javasimon.jdbc;

import org.javasimon.utils.Replacer;

import java.util.List;

/**
 * SqlNormalizer takes SQL statement and replaces parameters with question marks. It is
 * important to realize, that normalizer IS NOT SQL analyzer. It makes as simple replacement
 * as possible (with my coding skill ;-)) to still truly represent the original statement.
 * Normalized statement is merely used to recognize the original one and also to merge
 * the same statements with various arguments. Its primary purpose is to limit count of
 * distinct per-statement Simons. It doesn't suppose to be perfect or proof to all dialects.
 * <p/>
 * Usage is simple, you create normalizer with SQL statement and than you can ask the
 * object for normalizedSql and type via respective getters.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 17, 2008
 */
public final class SqlNormalizer {
	private static final Replacer[] FIRST_REPLACERS;
	private static final Replacer[] SECOND_REPLACERS;

	private static final Replacer FUNCTION_REPLACER = new Replacer("([-(=<>!+*/,]+\\s?)\\w+\\([^()]*\\)", "$1?", true);

	private static final Replacer TYPE_SELECTOR = new Replacer("^\\W*(\\w+)\\W.*", "$1");

	static {
		FIRST_REPLACERS = new Replacer[]{
			new Replacer("''", "?"), // replace empty strings and '' inside other strings
			new Replacer(" *([-=<>!+*/,]+) *", "$1"), // remove spaces around various operators and commas
			new Replacer("([-=<>!+*/]+)", " $1 "), // put spaces back (results in one space everywhere
			new Replacer("\\s+", " "), // normalize white spaces
			new Replacer("(create|alter|drop) (\\S+) ([^ (]+).*$", "$1 $2 $3"), // shring DLL to first three tokens
			new Replacer("([-=<>!+*/,.(]+\\s?)(?:(?:'[^']+')|(?:[0-9.]+))", "$1?"), // replace arguments after =, ( and , with ?
			new Replacer("like '[^']+'", "like ?"), // replace like arguments
			new Replacer("between \\S+ and \\S+", "between ? and ?"), // replace between arguments
			new Replacer(" in\\(", " in ("), // put space before ( in "in("
			new Replacer("^\\{|\\}$", ""), // remove { and } at the start/end
			new Replacer("^\\s*begin", "call"), // replace begin with call
			new Replacer(";?\\s*end;?$", ""), // remove final end
		};
		SECOND_REPLACERS = new Replacer[]{
			new Replacer(",", ", "), // put spaces after ,
			new Replacer(" in \\(\\?(?:, \\?)*\\)", " in (?)"), // shrink more ? in "in" to one
		};
	}

	private final String sql;
	private String normalizedSql;
	private String type;

	/**
	 * Creates SQL normalizer and performs the normalization.
	 *
	 * @param sql SQL to normalize
	 */
	public SqlNormalizer(String sql) {
		this.sql = sql;
		if (sql != null) {
			normalize(sql);
		}
	}

	/**
	 * Constructor for batch normalization. Type of the "statement" will be "batch".
	 *
	 * @param batch list of statements
	 */
	public SqlNormalizer(List<String> batch) {
		sql = "batch";
		StringBuilder sqlBuilder = new StringBuilder();
		String lastStmt = null;
		int stmtCounter = 0;
		for (String statement : batch) {
			normalize(statement);
			if (lastStmt == null) {
				lastStmt = normalizedSql;
			}
			if (!lastStmt.equalsIgnoreCase(normalizedSql)) {
				sqlBuilder.append(stmtCounter == 1 ? "" : stmtCounter + "x ").append(lastStmt).append("; ");
				lastStmt = normalizedSql;
				stmtCounter = 1;
			} else {
				stmtCounter++;
			}
		}
		sqlBuilder.append(stmtCounter == 1 ? "" : stmtCounter + "x ").append(lastStmt);
		type = "batch";
		this.normalizedSql = sqlBuilder.toString();
	}

	private void normalize(String sql) {
		normalizedSql = sql.toLowerCase().trim();
		applyReplacers(FIRST_REPLACERS);
		type = TYPE_SELECTOR.process(normalizedSql);

		// phase two - complications ;-)
		if (type.equals("select")) {
			String[] sa = normalizedSql.split(" from ", 2);
			if (sa.length == 2) {
				normalizedSql = sa[0] + " from " + FUNCTION_REPLACER.process(sa[1]);
			}
		} else {
			normalizedSql = FUNCTION_REPLACER.process(normalizedSql);
		}
		applyReplacers(SECOND_REPLACERS);
	}

	private void applyReplacers(Replacer[] replacers) {
		for (Replacer replacer : replacers) {
			normalizedSql = replacer.process(normalizedSql);
		}
	}

	/**
	 * Returns the original SQL.
	 *
	 * @return original SQL
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * Returns the normalized SQL.
	 *
	 * @return normalized SQL
	 */
	public String getNormalizedSql() {
		return normalizedSql;
	}

	/**
	 * Returns SQL type which is typicaly first word of the SQL (insert, select, etc). Returns batch for batches.
	 *
	 * @return SQL statement type or "batch"
	 */
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
