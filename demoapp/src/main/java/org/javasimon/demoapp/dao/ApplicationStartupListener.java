package org.javasimon.demoapp.dao;

import org.javasimon.demoapp.model.ToDoItem;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author Ivan Mushketyk (ivan.mushketyk@gmail.com)
 */
public class ApplicationStartupListener {
    @Autowired
    private ToDoItemDao toDoItemDao;

    @PostConstruct
    public void onApplicationStartup() {
        ToDoItem item1 = new ToDoItem();
        item1.setName("Try new awesome Javasimon feature");

        ToDoItem item2 = new ToDoItem();
        item2.setName("Write a post in my blog");
        item2.setDescription("How to use Javasimon for monitoring web applications");

        toDoItemDao.create(item1);
        toDoItemDao.create(item2);
    }

}
