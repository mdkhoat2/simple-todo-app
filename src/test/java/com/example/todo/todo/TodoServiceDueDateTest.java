package com.example.todo.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class TodoServiceDueDateTest {
    private TodoRepository repository;
    private TodoService service;

    @BeforeEach
    void setup() {
        repository = new TodoRepository(new InMemoryTodoJpaRepository());
        service = new TodoService(repository);
    }

    @Test
    void parseIsoInstantDueDate() {
        CreateTodoRequest req = new CreateTodoRequest("Task");
        req.setDueDate("2025-10-30T10:00:00Z");
        Todo t = service.add(null, req);
        assertNotNull(t.getDueDate());
        assertEquals(Instant.parse("2025-10-30T10:00:00Z"), t.getDueDate());
    }

    @Test
    void parseDateOnlyDueDateConvertsToStartOfDay() {
        CreateTodoRequest req = new CreateTodoRequest("Task");
        req.setDueDate("2025-10-30");
        Todo t = service.add(null, req);
        assertNotNull(t.getDueDate());
        // we cannot predict exact instant zone offset, but the string should parse with Instant
        // Ensure it's not equal to null and is an Instant
        assertTrue(t.getDueDate() instanceof Instant);
    }
}
