package org.javasimon.demoapp.model;

/**
 * Class that represents an item in a TODO list.
 *
 * @author Ivan Mushketyk (ivan.mushketyk@gmail.com)
 */
public class ToDoItem {
    private long id;
    private String name;
    private String description;
    private boolean done;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ToDoItem toDoItem = (ToDoItem) o;

        if (id != toDoItem.id) return false;
        if (done != toDoItem.done) return false;
        if (description != null ? !description.equals(toDoItem.description) : toDoItem.description != null)
            return false;
        if (name != null ? !name.equals(toDoItem.name) : toDoItem.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (done ? 1 : 0);
        return result;
    }
}
