package org.javasimon.demoapp.web;

import com.google.gson.Gson;
import org.javasimon.demoapp.dao.ToDoItemDao;
import org.javasimon.demoapp.model.ToDoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Mushketyk (ivan.mushketyk@gmail.com)
 */
@Controller
public class ToDoController {

    @Autowired
    private ToDoItemDao toDoItemDao;
    private Gson gson = new Gson();

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap model) {
        return "todo";
    }

    @RequestMapping(value="/items", method = RequestMethod.GET)
    public @ResponseBody String getAll(ModelMap model) {
        List<ToDoItem> items = toDoItemDao.getAll();

        return gson.toJson(items);
    }

    public void setToDoItemDao(ToDoItemDao toDoItemDao) {
        this.toDoItemDao = toDoItemDao;
    }
}
