package dbutils

import groovy.sql.GroovyRowResult
import groovy.sql.Sql

import java.sql.Timestamp

class DbInit {

	static Sql sql

	/**
	 * If false, then map "defaults" in findOrCreate is used only for inserts.
	 * If true, it is used to update as well.
	 */
	static def updateWithDefaults = false
	static def verbose = false
	/** If true, dates are converted to timestamp - handy for old MSSQL 2005. */
	static def datetimeOnly = true

	private static int inserts;
	private static int deletes;
	private static int deletedRows;
	private static int updates;
	private static int updatedRows;

	static void connect(url, user, password) {
		if (sql != null) {
			println "=== Closing existing connection $sql"
			sql.close()
		}
		println "=== Connecting to $url, user $user"
		sql = Sql.newInstance(url, user, password)
		inserts = deletes = deletedRows = updates = updatedRows = 0
	}

	/**
	 * Universal (nearly) find/update/create method.
	 * <ul>
	 * <li>Object is found in a table using selector map.
	 * <li>If insertParams are present, object is updated accordingly.
	 * <li>If insertParams is null object (or null if not found) is return - no insert/update
	 * is executed.
	 * <li>If object is not found and insertParams are not null (they can be if we only want to
	 * find object) we try to insert it.
	 * <li>If insert is required provide at least [:] as insertParams, preferably specify also
	 * defaults for user convenience.
	 * <li>If insert is executed effective insertParams are constructed as: selector (without
	 * any keys with operators though) + defaults + insertParams. For this reason any selector
	 * entries with operators (for instance 'flags & 2':2) should appear with matching explicit
	 * value in insertParams (e.g. flags:3, that will be found by condition flags & 2 = 2).
	 * Otherwise subsequent runs may not find previously created entity - which may cause
	 * constraint violation or their repetition. Not sure what is worse. :-)
	 * <li>If object is found but differences are detected against insertMap it will be updated.
	 * <li>Default map is used for inserts or for updates as well, based on
	 * {@link #updateWithDefaults} boolean flag. Can be null, then acts like [:].
	 * In any case, values in insertParams always override defaults.
	 * <li>If insert is executed, id is always returned (or list of them if there is not
	 * single autoincrement).
	 * <li>If entity is just found or updated, id is returned based on "resultProperty"
	 * parameter, that defaults to 'id'.
	 * <li>If resultProperty is set to null, the whole object is returned.
	 * <li>Selector implies = operation, but allows for other using # in the name of the key,
	 * e.g. 'col#>':5 will result in 'col > 5' condition.
	 * <li>If 'col#is ...' is used as a key, 'col is ...' is used (NULL check). "Falsy" value is
	 * ignored, "truthy" value is added to params, <b>but user is responsible for adding ?.</b>
	 * <li>Selector's key can be column or other expression, e.g. flags & 2. Equality is still
	 * implied, but this can be combined with # construct, e.g.: 'sqrt(col)#>':5
	 * </ul>
	 * Column list and values with question-marks is constructed automatically from merged
	 * selector and insertParams map.
	 */
	static Object findOrCreate(String tableName, Map selector,
		Map insertParams = null, Map defaults = [:], String resultProperty = 'id')
	{
		defaults = defaults ?: [:]

		// here we construct where part and list of actual parameters
		def (String wherePart, List<Object> whereParams) = processSelector(selector)
		def query = "select * from $tableName where $wherePart"
		debug(query, whereParams)
		def object = sql.firstRow(query, whereParams)
		def objectOrId = objectOrId(object, resultProperty)
		if (object != null) {
			// defaults must always go first in +, so they can be overridden
			if (insertParams != null
				&& updateNeeded(object, (updateWithDefaults ? defaults : [:]) + insertParams))
			{
				// UPDATE time :-)
				updateInternal(tableName, wherePart, whereParams, defaults + insertParams)
				println "$tableName UPDATED (id=$objectOrId)"
			}
			return objectOrId
		}
		if (insertParams == null) {
			println "$tableName NOT FOUND"
			return null
		}

		// here we construct column list, question marks and list of actual parameters
		def pureSelector = purifySelector(selector)
		List<List<Object>> keys = insert(tableName, pureSelector + defaults + insertParams)
		// lastly we extract single key of possible, otherwise we return the whole row
		// of returned autoincrement values (unlikely)
		def id = keys[0]
		if (id.size() == 1) {
			id = id[0]
		}
		println "$tableName INSERTED (id=$id)"
		return id
	}

	private static int updateInternal(
		String tableName, String wherePart, List<Object> whereParams, Map params)
	{
		def (String setPart, List<Object> updateParams) = processUpdateMap(params)
		updates++
		def finalUpdateParams = updateParams + whereParams
		def query = (wherePart != null
			? "update $tableName set $setPart where $wherePart"
			: "update $tableName set $setPart")
		debug(query, finalUpdateParams)
		def cnt = sql.executeUpdate(query, finalUpdateParams)
		updatedRows += cnt
		return cnt
	}

	/**
	 * Update table, where goes first, update params second
	 * (unlike SQL, but in line with {@link #findOrCreate(java.lang.String, java.util.Map)}).
	 */
	static int update(String tableName, Map selector, Map params) {
		def (String wherePart, List<Object> whereParams) = processSelector(selector)
		def cnt = updateInternal(tableName, wherePart, whereParams, params)
		println "$tableName UPDATED: $cnt"
		return cnt;
	}

	private static Object objectOrId(GroovyRowResult object, String resultProperty) {
		resultProperty != null && object != null ? object[resultProperty] : object
	}

	/** Removes any keys with operators and returns "purified" map. */
	private static Map purifySelector(Map map) {
		def result = [:]
		for (String key : map.keySet()) {
			if (key.indexOf('#') != -1 || key.indexOf('&') != -1) {
				continue
			} // other operator, just skip it

			result.put(key, map.get(key))
		}
		return result
	}

	/** Selects first matching object from the table based on selector. */
	static Object find(String tableName, Map selector) {
		def (String wherePart, List<Object> whereParams) = processSelector(selector)
		def query = "select * from $tableName where $wherePart"
		debug(query, whereParams)
		return sql.firstRow(query, whereParams)
	}

	/**
	 * Selects first matching object from the table based on selector,
	 * throws exception if not exactly 1 result.
	 */
	static Object findUnique(String tableName, Map selector) {
		def results = list(tableName, selector)
		if (results.size() > 1) {
			throw new RuntimeException("Too many results for $tableName: $selector")
		}
		if (results.isEmpty()) {
			throw new RuntimeException("No result for $tableName: $selector")
		}
		return results[0]
	}

	/** Returns list from table based on selector. */
	static List<GroovyRowResult> list(String tableName, Map selector = [:]) {
		def (String wherePart, List<Object> whereParams) = processSelector(selector)
		def query = "select * from $tableName" + (wherePart ? " where $wherePart" : "")
		debug(query, whereParams)
		return sql.rows(query, whereParams)
	}

	/** Deletes from table based on selector. */
	static int delete(String tableName, Map selector = null) {
		def (String wherePart, List<Object> whereParams) = processSelector(selector)
		// we need to take out the Gstring from executeUpdate, because it's interpreted
		// there more than we want (IN would not work)
		def query = (selector != null
			? "delete from $tableName where $wherePart"
			: "delete from $tableName")
		debug(query, whereParams)
		deletes++
		def cnt = sql.executeUpdate(query, whereParams)
		deletedRows += cnt
		println "$tableName DELETED: $cnt"
		return cnt
	}

	private static List processSelector(Map selector) {
		return processSelectorAndUpdate(selector, ' and ')
	}

	static boolean updateNeeded(GroovyRowResult rowResult, Map requestedValues) {
		for (entry in requestedValues.entrySet()) {
			def currentValue = rowResult.get(entry.getKey())
			def requestedValue = entry.getValue()
			if (currentValue != requestedValue) {
				return true
			}
		}
		return false
	}

	private static List processUpdateMap(Map selector) {
		return processSelectorAndUpdate(selector, ',')
	}

	private static List processSelectorAndUpdate(Map selector, String separator) {
		if (selector == null) {
			return [null, []]
		}
		def keySet = selector.keySet()
		def sb = new StringBuilder()
		def params = []
		for (String key in keySet) {
			if (sb.length() > 0) {
				sb.append(separator)
			}
			def colWithOp = key
			def value = selector.get(key)
			def opSeparatorIndex = colWithOp.indexOf('#')
			if (opSeparatorIndex != -1) {
				sb.append(colWithOp.replace('#', ' '))
				// for #IS cases we don't want to add parameter
				// unless it's "truthy"(and user must provide ? in the key)
				if (colWithOp.length() > opSeparatorIndex + 3
					&& colWithOp.substring(opSeparatorIndex + 1, opSeparatorIndex + 3).equalsIgnoreCase("IS"))
				{
					if (value) {
						params.add(value)
					}
					continue
				}
			} else {
				sb.append(colWithOp).append(' =')
			}
			sb.append(' ?')
			params.add(value)
		}
		return [sb.toString(), params]
	}

	/** Simple insert into table, no default values. */
	static List<List<Object>> insert(String tableName, Map params) {
		def (String insertColumns, String questionMarks, List<Object> insertParams) = processInsertMap(params)
		def query = 'insert into ' + tableName +
			' (' + insertColumns + ') values (' + questionMarks + ')'
		debug(query, insertParams)
		inserts++
		return sql.executeInsert(query, insertParams)
	}

	private static List processInsertMap(Map insertParams) {
		def keySet = insertParams.keySet()
		def insertColumns = keySet.join(",")
		def sb = new StringBuilder()
		def params = []
		for (key in keySet) {
			if (sb.length() > 0) {
				sb.append(',')
			}
			sb.append('?')
			params.add(insertParams.get(key))
		}
		return [insertColumns, sb.toString(), params]
	}

	static void query(String query) {
		sql.execute(query)
	}

	static void joinWithMany(String relationTable, String oneIdColumn,
		String manyIdColumn, Integer oneId, List<Integer> manyIds)
	{
		if (manyIds != null) {
			def currentRoleIds = list(relationTable, [(oneIdColumn): oneId]).collect {
				it[manyIdColumn]
			}
			def missingRoles = manyIds - currentRoleIds
			if (!missingRoles.isEmpty()) {
				println "Adding missing roles: " + missingRoles
				missingRoles.forEach {
					insert(relationTable, [(oneIdColumn): oneId, (manyIdColumn): it])
				}
			}
			def excessiveRoles = currentRoleIds - manyIds
			if (!excessiveRoles.isEmpty()) {
				println "Removing excessive roles: " + excessiveRoles
				excessiveRoles.forEach {
					delete(relationTable, [(oneIdColumn): oneId, (manyIdColumn): it])
				}
			}
		}
	}

	static def parseIsoDate(String date) {
		return sqlDateType(Date.parse('yyyy-MM-dd', date))
	}

	static def sqlDateType(Object temporal) {
		long value = 0;
		if (temporal instanceof Date) {
			value = temporal.time
		}
		return (DbInit.datetimeOnly
			? new Timestamp(value)
			: new java.sql.Date(value))
	}

	private static void debug(Object... objectsOrMessages) {
		if (verbose) {
			objectsOrMessages.each { println "DEBUG: " + it }
		}
	}

	static void printStatistics() {
		println "\ninserts = $inserts"
		println "updates = $updates (rows $updatedRows)"
		println "deletes = $deletes (rows $deletedRows)"
	}
}