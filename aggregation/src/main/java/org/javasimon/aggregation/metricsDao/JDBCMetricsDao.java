package org.javasimon.aggregation.metricsDao;

import org.javasimon.aggregation.MetricsDao;
import org.javasimon.jmx.StopwatchSample;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class JDBCMetricsDao implements MetricsDao {

	public static final String STOPWATCH_TABLE = "stopwatch";
	private static final String CREATE_STOPWATCH_TABLE =
			"CREATE TABLE stopwatch(managerId varchar(128), " +
					"name varchar(256)," +
					"mean DOUBLE PRECISION," +
					"standardDeviation DOUBLE PRECISION," +
					"variance DOUBLE PRECISION," +
					"varianceN DOUBLE PRECISION," +
					"note varchar(256)," +
					"firstUsage INTEGER," +
					"lastUsage INTEGER," +
					"lastReset INTEGER," +
					"total INTEGER," +
					"counter INTEGER," +
					"min INTEGER," +
					"max INTEGER," +
					"minTimestamp INTEGER," +
					"maxTimestamp INTEGER," +
					"active INTEGER," +
					"maxActive INTEGER," +
					"maxActiveTimestamp INTEGER," +
					"last INTEGER);";

	private SimpleJdbcInsert stopwatchInsert;
	private JdbcTemplate jdbcTemplate;

	public JDBCMetricsDao(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		stopwatchInsert = new SimpleJdbcInsert(dataSource).withTableName(STOPWATCH_TABLE);
	}

	public void init() throws DaoException {
		jdbcTemplate.update(CREATE_STOPWATCH_TABLE);
	}

	@Override
	public void storeStopwatchSamples(String managerId, List<StopwatchSample> samples) throws DaoException {
		for (StopwatchSample sample : samples) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("managerId", managerId);
			parameters.put("name", sample.getName());
			parameters.put("mean", sample.getMean());
			parameters.put("standardDeviation", sample.getStandardDeviation());
			parameters.put("variance", sample.getVariance());
			parameters.put("varianceN", sample.getVarianceN());
			parameters.put("note", sample.getNote());
			parameters.put("firstUsage", sample.getFirstUsage());
			parameters.put("lastUsage", sample.getLastUsage());
			parameters.put("lastReset", sample.getLastReset());
			parameters.put("total", sample.getTotal());
			parameters.put("counter", sample.getCounter());
			parameters.put("min", sample.getMin());
			parameters.put("max", sample.getMax());
			parameters.put("minTimestamp", sample.getMinTimestamp());
			parameters.put("maxTimestamp", sample.getMaxTimestamp());
			parameters.put("active", sample.getActive());
			parameters.put("maxActive", sample.getMaxActive());
			parameters.put("maxActiveTimestamp", sample.getMaxTimestamp());
			parameters.put("last", sample.getLast());

			stopwatchInsert.execute(parameters);
		}
	}

	@Override
	public List<StopwatchSample> getStopwatchSamples(String managerId) {
		List<StopwatchSample> items =
				jdbcTemplate.query(
						"SELECT name, mean, standardDeviation, variance, varianceN, note, firstUsage, lastUsage, " +
						"lastReset, total, counter, min, max, minTimestamp, maxTimestamp, active, maxActive, " +
						"maxActiveTimestamp, last " +
						"FROM stopwatch " +
						"WHERE stopwatch.managerId = ?",
						new Object[]{managerId},
						new StopwatchSampleRowMapper());

		return items;
	}

	class StopwatchSampleRowMapper implements RowMapper<StopwatchSample> {

		@Override
		public StopwatchSample mapRow(ResultSet resultSet, int line) throws SQLException {
			org.javasimon.StopwatchSample sample = new org.javasimon.StopwatchSample();
			sample.setName(resultSet.getString(1));
			sample.setMean(resultSet.getDouble(2));
			sample.setStandardDeviation(resultSet.getDouble(3));
			sample.setVariance(resultSet.getDouble(4));
			sample.setVariance(resultSet.getDouble(5));
			sample.setNote(resultSet.getString(6));
			sample.setFirstUsage(resultSet.getLong(7));
			sample.setLastUsage(resultSet.getLong(8));
			sample.setTotal(resultSet.getLong(9));
			sample.setCounter(resultSet.getLong(10));
			sample.setMin(resultSet.getLong(11));
			sample.setMax(resultSet.getLong(12));
			sample.setMinTimestamp(resultSet.getLong(13));
			sample.setMaxTimestamp(resultSet.getLong(14));
			sample.setActive(resultSet.getLong(15));
			sample.setMaxActive(resultSet.getLong(16));
			sample.setMaxActiveTimestamp(resultSet.getLong(17));
			sample.setLast(resultSet.getLong(18));

			return new StopwatchSample(sample);
		}
	}
}
