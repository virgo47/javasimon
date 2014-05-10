package org.javasimon.demoapp.web;

import com.google.gson.*;
import org.javasimon.demoapp.dao.DaoException;
import org.javasimon.demoapp.dao.ToDoItemDao;
import org.javasimon.demoapp.model.ToDoItem;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

/**
 * @author Ivan Mushketyk (ivan.mushketyk@gmail.com)
 */
public class ToDoControllerTest {

    private ToDoController controller;
    private ToDoItemDao dao;

    private Gson gson = new Gson();

    @BeforeMethod
    public void beforeMethod() {
        dao = mock(ToDoItemDao.class);
        controller = new ToDoController();
        controller.setToDoItemDao(dao);
    }

    @Test
    public void getAll() {
        List<ToDoItem> items = new ArrayList<>();
        ToDoItem item = new ToDoItem();
        item.setDone(false);
        item.setDescription("Item description");
        item.setName("Item name");
        items.add(item);

        when(dao.getAll()).thenReturn(items);

        JsonArray expectedArray = new JsonArray();
        JsonObject object = new JsonObject();
        object.addProperty("done", false);
        object.addProperty("name", "Item name");
        object.addProperty("description", "Item description");
        object.addProperty("id", 0);

        expectedArray.add(object);

        String str = controller.getAll(new ModelMap());
        JsonElement actualJson = parseJson(str);
        assertEquals(expectedArray, actualJson);
    }

    @Test
    public void deleteItem() {
        long idToRemove = 123;
        ResponseEntity<String> result = controller.deleteItem(123);

        verify(dao).delete(idToRemove);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteNonExistingItemShouldCauseException() {
        long idToRemove = 123;
        Mockito.doThrow(new DaoException()).when(dao).delete(idToRemove);
        ResponseEntity<String> result = controller.deleteItem(123);

        verify(dao).delete(idToRemove);
        assertEquals(result.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void addNewItem() {
        ToDoItem item = new ToDoItem();
        item.setName("name");
        item.setDescription("description");

        controller.addItem(item);

        verify(dao).create(item);
       // assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void updateItem() {
        ToDoItem item = new ToDoItem();
        item.setId(123);
        item.setName("name");
        item.setDescription("description");

        controller.updateItem(item);

        verify(dao).update(item);
    }

    private JsonElement parseJson(String str) {
        return new JsonParser().parse(str);
    }

}

