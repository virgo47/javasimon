package org.javasimon.demoapp.dao;

import org.javasimon.demoapp.model.ToDoItem;

import java.util.List;

/**
 * @author Ivan Mushketyk (ivan.mushketyk@gmail.com)
 */
public interface ToDoItemDao {
    List<ToDoItem> getAll();

    void create(ToDoItem toDoItem);

    void delete(long id);

    void update(ToDoItem toDoItem);

    ToDoItem getById(long id);

    void deleteAll();
}
