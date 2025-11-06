package com.example.todo.todo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateTodoRequestTest {

    @Test
    void gettersAndSettersWork() {
        UpdateTodoRequest req = new UpdateTodoRequest();
        req.setTitle("New Title");
        req.setCompleted(Boolean.TRUE);
        req.setDueDate("2025-12-31");
        req.setPriority("HIGH");

        assertEquals("New Title", req.getTitle());
        assertEquals(Boolean.TRUE, req.getCompleted());
        assertEquals("2025-12-31", req.getDueDate());
        assertEquals("HIGH", req.getPriority());
    }
}
