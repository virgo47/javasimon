package org.javasimon.demoapp.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * DataModel.
 *
 * @author virgo47@gmail.com
 */
@Repository
public class Dao {
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public Person selectPerson(int id) throws SQLException {
		return jdbcTemplate.queryForObject(
			"select id, login, name, address from person where id = ?", new PersonMapper(), id);
	}

	public List<Person> listPersons() throws SQLException {
		return jdbcTemplate.query(
			"select id, login, name, address from person where id = ?", new PersonMapper());
	}

	public void insertPerson(final Person person) throws SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
			new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(
						"insert into person (login, name, address) values (?, ?, ?)", new String[]{"id"});
					ps.setString(1, person.getLogin());
					ps.setString(1, person.getName());
					ps.setString(1, person.getAddress());
					return ps;
				}
			},
			keyHolder);
		person.setId(keyHolder.getKey().intValue());
	}

	public void updatePerson(Person person) throws SQLException {
		jdbcTemplate.update("update person set login = ?, name = ?, address = ? where id = ?)",
			person.getLogin(), person.getName(), person.getAddress(), person.getId());
	}

	public void deletePerson(int id) throws SQLException {
		jdbcTemplate.update("delete from person where id = ?", id);
	}

	private class PersonMapper implements RowMapper<Person> {
		@Override
		public Person mapRow(ResultSet resultSet, int i) throws SQLException {
			Person person = new Person();
			person.setId(resultSet.getInt(1));
			person.setLogin(resultSet.getString(2));
			person.setName(resultSet.getString(3));
			person.setAddress(resultSet.getString(4));
			return person;
		}
	}
}
