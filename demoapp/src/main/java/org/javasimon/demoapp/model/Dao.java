package org.javasimon.demoapp.model;

import java.sql.*;

/**
 * DataModel.
 *
 * @author virgo47@gmail.com
 */
public class Dao {
	private Connection sqlConnection;

	public Dao(Connection sqlConnection) {
		this.sqlConnection = sqlConnection;
	}

	public Person selectPerson(int id) throws SQLException {
		try (PreparedStatement statement = sqlConnection.prepareStatement("select * from person where id = ?")) {
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				Person person = new Person();
				person.setId(resultSet.getInt(1));
				person.setLogin(resultSet.getString(2));
				person.setName(resultSet.getString(3));
				person.setAddress(resultSet.getString(4));
				return person;
			}
			return null;
		}
	}

	public void insertPerson(Person person) throws SQLException {
		try (PreparedStatement statement = sqlConnection.prepareStatement("insert into person values (?, ?, ?, ?)")) {
			statement.setNull(1, Types.INTEGER);
			statement.setString(2, person.getLogin());
			statement.setString(3, person.getName());
			statement.setString(4, person.getAddress());
			statement.execute();
			ResultSet generatedKeys = statement.getGeneratedKeys();
			generatedKeys.next();
			person.setId(generatedKeys.getInt(1));
		}
	}

	public void updatePerson(Person person) throws SQLException {
		try (PreparedStatement statement = sqlConnection.prepareStatement(
			"update person set login = ?, name = ?, address = ? where id = ?)")) {
			statement.setString(1, person.getLogin());
			statement.setString(2, person.getName());
			statement.setString(3, person.getAddress());
			statement.setInt(4, person.getId());
			statement.execute();
		}
	}

	public void deletePerson(int id) throws SQLException {
		try (PreparedStatement statement = sqlConnection.prepareStatement("delete from person where id = ?")) {
			statement.setInt(1, id);
			statement.execute();
		}
	}
}
