package com.example.todo.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TodoServiceTest {
    private TodoRepository repository;
    private TodoService service;

    @BeforeEach
    void setup() {
        repository = new TodoRepository(new InMemoryTodoJpaRepository());
        repository.clear();
        service = new TodoService(repository);
    }

    @Test
    void addAndList() {
        Todo t1 = service.add("Write tests");
        Todo t2 = service.add("Wire CI");
        List<Todo> all = service.list();
        assertEquals(2, all.size());
        assertEquals(t1.getId(), all.get(0).getId());
        assertEquals(t2.getId(), all.get(1).getId());
        assertFalse(all.get(0).isCompleted());
    }

    @Test
    void toggleChangesCompletedFlag() {
        Todo t = service.add("Do something");
        assertFalse(t.isCompleted());
        Todo toggled = service.toggle(t.getId());
        assertTrue(toggled.isCompleted());
        Todo toggledAgain = service.toggle(t.getId());
        assertFalse(toggledAgain.isCompleted());
    }

    @Test
    void deleteRemovesTodo() {
        Todo t = service.add("Remove me");
        service.delete(t.getId());
        assertEquals(0, service.list().size());
    }

    @Test
    void togglingUnknownIdThrows() {
        assertThrows(TodoNotFoundException.class, () -> service.toggle(999));
    }

    @Test
    void deleteUnknownIdThrows() {
        assertThrows(TodoNotFoundException.class, () -> service.delete(999));
    }

    @Test
    void addRejectsBlankTitle() {
        assertThrows(IllegalArgumentException.class, () -> service.add("   "));
    }
}
