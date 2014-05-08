package org.javasimon.demoapp.dao;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;

import org.javasimon.demoapp.model.ToDoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author Ivan Mushketyk (ivan.mushketyk@gmail.com)
 */
@ContextConfiguration(locations = {"classpath:/servlet-context.xml"})
public class ToDoDaoImplTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ToDoItemDaoImpl toDoDao;

	@BeforeMethod
	public void beforeMethod() {
		toDoDao.deleteAll();
	}

	@Test
	public void addToDoItem() {
		ToDoItem item = createItem("name");
		toDoDao.create(item);

		List<ToDoItem> items = toDoDao.getAll();
		assertEquals(items.size(), 1);
		assertEquals(items.get(0), item);
	}

	@Test
	public void uniqueIdsAreGenerated() {
		ToDoItem item1 = createItem("name");
		ToDoItem item2 = createItem("name");

		toDoDao.create(item1);
		toDoDao.create(item2);

		List<ToDoItem> items = toDoDao.getAll();
		assertEquals(items.size(), 2);
		assertNotEquals(items.get(0).getId(), items.get(1).getId());
	}

	@Test
	public void getByIdFromEmptyStorage() {
		ToDoItem item = toDoDao.getById(0);
		assertNull(item);
	}

	@Test
	public void getById() {
		ToDoItem item = createItem("name");
		toDoDao.create(item);

		ToDoItem actual = toDoDao.getById(item.getId());
		assertEquals(actual, item);
	}

	@Test
	public void deleteItem() {
		ToDoItem item = createItem("name");
		toDoDao.create(item);

		toDoDao.delete(item.getId());
		List<ToDoItem> items = toDoDao.getAll();
		assertEquals(items.size(), 0);
	}

	@Test(expectedExceptions = DaoException.class)
	public void deleteNonExistingItemCausesException() {
		toDoDao.delete(12345);
	}

	@Test
	public void updateItem() {
		String oldName = "name";
		ToDoItem item = createItem(oldName);
		toDoDao.create(item);

		String newName = "newName";
		ToDoItem newItem = createItem(newName);
		newItem.setId(item.getId());

		toDoDao.update(newItem);

		ToDoItem actualItem = toDoDao.getById(item.getId());
		assertEquals(actualItem.getName(), newName);
	}

	@Test(expectedExceptions = DaoException.class)
	public void updateNonExistingItemShouldCauseException() {
		ToDoItem item = createItem("newName");
		item.setId(12345);
		toDoDao.update(item);
	}

	private ToDoItem createItem(String name) {
		ToDoItem item = new ToDoItem();
		item.setName(name);

		return item;
	}
}
