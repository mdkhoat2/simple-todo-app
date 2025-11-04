package com.example.todo.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TodoRepositoryTest {
    private TodoRepository repo;

    @BeforeEach
    void setup() {
        repo = new TodoRepository();
    }

    @Test
    void perUserStoresAreIsolated() {
    repo.saveForUser("alice", "Task A1", null, null);
    repo.saveForUser("alice", "Task A2", null, null);

    repo.saveForUser("bob", "Task B1", null, null);

        List<Todo> aliceList = repo.findAllForUser("alice");
        List<Todo> bobList = repo.findAllForUser("bob");

        assertEquals(2, aliceList.size());
        assertEquals(1, bobList.size());

        assertTrue(aliceList.stream().anyMatch(t -> t.getTitle().equals("Task A1")));
        assertTrue(bobList.stream().anyMatch(t -> t.getTitle().equals("Task B1")));

        // Ensure ids are independent per user (both start from 1)
        assertEquals(1, aliceList.get(0).getId());
        assertEquals(1, bobList.get(0).getId());
    }

    @Test
    void saveSetsCreatedAtAndDueDateStored() {
        Instant now = Instant.now();
        Todo t = repo.saveForUser("alice", "Has due", now, "high");
        assertNotNull(t.getCreatedAt());
        assertEquals(now, t.getDueDate());
        assertEquals("high", t.getPriority());
    }
}
