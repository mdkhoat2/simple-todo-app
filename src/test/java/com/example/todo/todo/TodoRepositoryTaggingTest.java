package com.example.todo.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TodoRepositoryTaggingTest {
    private TodoRepository repo;

    @BeforeEach
    void setup() {
        repo = new TodoRepository(new InMemoryTodoJpaRepository());
    }

    @Test
    void addAndRemoveTagsWork() {
        Todo t = repo.saveForUser("u1", "T1", null, null, new java.util.ArrayList<>(List.of("a")));
        assertNotNull(t.getId());
        assertEquals(1, t.getTags().size());

        // add new tag
        Todo added = repo.addTagForUser("u1", t.getId(), "b").orElseThrow();
        assertTrue(added.getTags().contains("b"));

        // adding duplicate shouldn't duplicate
        Todo still = repo.addTagForUser("u1", t.getId(), "b").orElseThrow();
        long countB = still.getTags().stream().filter(s -> s.equals("b")).count();
        assertEquals(1, countB);

        // remove tag
        Todo removed = repo.removeTagForUser("u1", t.getId(), "a").orElseThrow();
        assertFalse(removed.getTags().contains("a"));

        // removing non-existing tag returns same (no crash)
        Todo same = repo.removeTagForUser("u1", t.getId(), "doesnotexist").orElseThrow();
        assertNotNull(same);
    }

    @Test
    void perUserIsolationWithTags() {
        Todo t1 = repo.saveForUser("alice", "A1", null, null, new java.util.ArrayList<>(List.of("x")));
        Todo t2 = repo.saveForUser("bob", "B1", null, null, new java.util.ArrayList<>(List.of("x")));

        // add tag to alice only
        repo.addTagForUser("alice", t1.getId(), "aliceOnly");
        Todo a = repo.findByIdForUser("alice", t1.getId()).orElseThrow();
        Todo b = repo.findByIdForUser("bob", t2.getId()).orElseThrow();

        assertTrue(a.getTags().contains("aliceOnly"));
        assertFalse(b.getTags().contains("aliceOnly"));
    }
}
