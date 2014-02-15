package org.javasimon.demoapp.web;

import com.google.gson.Gson;
import org.javasimon.demoapp.dao.DaoException;
import org.javasimon.demoapp.dao.ToDoItemDao;
import org.javasimon.demoapp.model.ToDoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ivan Mushketyk (ivan.mushketyk@gmail.com)
 */
@Controller
public class ToDoController {

    @Autowired
    private ToDoItemDao toDoItemDao;
    private Gson gson = new Gson();

    @RequestMapping(value="/items", method = RequestMethod.GET)
    public @ResponseBody String getAll(ModelMap model) {
        List<ToDoItem> items = toDoItemDao.getAll();

        return gson.toJson(items);
    }

    @RequestMapping(value="/addItem", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ToDoItem addItem(@RequestBody final ToDoItem item) {
        toDoItemDao.create(item);
        return item;
    }

    @RequestMapping(value="/deleteItem/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteItem(@PathVariable("id") long id) {
        try {
            toDoItemDao.delete(id);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (DaoException ex) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/updateItem", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ToDoItem updateItem(@RequestBody final ToDoItem item) {
        toDoItemDao.update(item);
        return item;
    }

    public void setToDoItemDao(ToDoItemDao toDoItemDao) {
        this.toDoItemDao = toDoItemDao;
    }
}
