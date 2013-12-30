package org.javasimon.demoapp.dao;

import org.javasimon.demoapp.model.ToDoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Ivan Mushketyk (ivan.mushketyk@gmail.com)
 */
@Repository
public class ToDoItemDaoImpl implements ToDoItemDao {
    private DataSource dataSource;
    private SimpleJdbcInsert insert;
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void postConstruct() {
        insert = new SimpleJdbcInsert(dataSource).withTableName("toDoItem").usingGeneratedKeyColumns("id");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    @Qualifier("dataSource")
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public List<ToDoItem> getAll() {
        return jdbcTemplate
                .query("SELECT id, name, description, isDone FROM toDoItem",
                        new Object[]{},
                        new ToDoItemRowMapper());
    }

    @Override
    public void create(ToDoItem toDoItem) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", toDoItem.getName());
        parameters.put("description", toDoItem.getDescription());
        parameters.put("isDone", toDoItem.isDone());
        Number newId = insert.executeAndReturnKey(parameters);
        toDoItem.setId(newId.longValue());
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM toDoItem WHERE id = ?",
                new Object[] {id});
    }

    @Override
    public void update(ToDoItem newItem) {
        jdbcTemplate.update("UPDATE toDoItem SET name = ?, description = ?, isDone = ? WHERE id = ?",
                new Object[] {newItem.getName(), newItem.getDescription(), newItem.isDone(), newItem.getId()});
    }

    @Override
    public ToDoItem getById(long id) {
        List<ToDoItem> items =
                jdbcTemplate.query("SELECT id, name, description, isDone FROM toDoItem WHERE toDoItem.id = ?",
                        new Object[]{id},
                        new ToDoItemRowMapper());

        return items.isEmpty() ? null : items.get(0);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM toDoItem");
    }
}

class ToDoItemRowMapper implements RowMapper<ToDoItem> {

    @Override
    public ToDoItem mapRow(ResultSet resultSet, int line) throws SQLException {
        ToDoItem toDoItem = new ToDoItem();
        toDoItem.setId(resultSet.getLong(1));
        toDoItem.setName(resultSet.getString(2));
        toDoItem.setDescription(resultSet.getString(3));
        toDoItem.setDone(resultSet.getBoolean(4));

        return toDoItem;
    }
}

