package com.example.todo.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TodoServiceTaggingTest {
    private TodoService service;

    @BeforeEach
    void setup() {
        service = new TodoService(new TodoRepository(new InMemoryTodoJpaRepository()));
    }

    @Test
    void listFiltersByTag() {
        service.add("alice", new CreateTodoRequest("T1"));
        // create with tags directly via repository to ensure tags exist
        TodoRepository repo = new TodoRepository(new InMemoryTodoJpaRepository());
        // use same backing repository for service
        // workaround: create a fresh service backed by repo
        service = new TodoService(repo);
        repo.saveForUser("alice", "A1", null, null, List.of("x","y"));
        repo.saveForUser("alice", "A2", null, null, List.of("y"));
        repo.saveForUser("alice", "A3", null, null, List.of("z"));

        var filtered = service.list("alice", "y");
        assertEquals(2, filtered.size());
        assertTrue(filtered.stream().allMatch(t -> t.getTags() != null && t.getTags().contains("y")));
    }

    @Test
    void addAndRemoveTagViaService() {
        TodoRepository repo = new TodoRepository(new InMemoryTodoJpaRepository());
        service = new TodoService(repo);
        Todo t = repo.saveForUser("u", "Hello", null, null, null);

        Todo withTag = service.addTag("u", t.getId(), "new");
        assertTrue(withTag.getTags().contains("new"));

        Todo removed = service.removeTag("u", t.getId(), "new");
        assertFalse(removed.getTags().contains("new"));
    }
}
