package com.example.todo.todo;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TodoRepository {
    // Per-user in-memory stores. Key is userId string (can be generated UUID or any identifier).
    // For backward compatibility, an operation without a userId will use the DEFAULT_USER key.
    private static final String DEFAULT_USER = "__default__";

    private static class UserStore {
        final ConcurrentMap<Long, Todo> todos = new ConcurrentHashMap<>();
        final AtomicLong seq = new AtomicLong(0);
    }

    private final ConcurrentMap<String, UserStore> users = new ConcurrentHashMap<>();

    private UserStore getOrCreate(String userId) {
        String key = userId == null ? DEFAULT_USER : userId;
        return users.computeIfAbsent(key, k -> new UserStore());
    }

    // Backwards-compatible operations (no user) -> default user
    public Todo save(String title) {
        return saveForUser(null, title, null, null);
    }

    public Todo save(String title, Instant dueDate, String priority) {
        return saveForUser(null, title, dueDate, priority);
    }

    public List<Todo> findAll() {
        return findAllForUser(null);
    }

    public Optional<Todo> findById(long id) {
        return findByIdForUser(null, id);
    }

    public Optional<Todo> toggle(long id) {
        return toggleForUser(null, id);
    }

    public Optional<Todo> update(Todo todo) {
        return updateForUser(null, todo);
    }

    public boolean delete(long id) {
        return deleteForUser(null, id);
    }

    public void clear() {
        UserStore s = users.get(DEFAULT_USER);
        if (s != null) {
            s.todos.clear();
            s.seq.set(0);
        }
    }

    // Per-user API
    public Todo saveForUser(String userId, String title, Instant dueDate, String priority) {
        UserStore s = getOrCreate(userId);
        long id = s.seq.incrementAndGet();
        Todo todo = new Todo(id, title, false, Instant.now(), dueDate, priority);
        s.todos.put(id, todo);
        return todo;
    }

    public List<Todo> findAllForUser(String userId) {
        UserStore s = getOrCreate(userId);
        Collection<Todo> values = s.todos.values();
        List<Todo> list = new ArrayList<>(values);
        list.sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
        return Collections.unmodifiableList(list);
    }

    public Optional<Todo> findByIdForUser(String userId, long id) {
        UserStore s = getOrCreate(userId);
        return Optional.ofNullable(s.todos.get(id));
    }

    public Optional<Todo> toggleForUser(String userId, long id) {
        UserStore s = getOrCreate(userId);
        return Optional.ofNullable(s.todos.computeIfPresent(id, (k, v) -> v.toggle()));
    }

    public Optional<Todo> updateForUser(String userId, Todo todo) {
        UserStore s = getOrCreate(userId);
        return Optional.ofNullable(s.todos.computeIfPresent(todo.getId(), (k, v) -> todo));
    }

    public boolean deleteForUser(String userId, long id) {
        UserStore s = getOrCreate(userId);
        return s.todos.remove(id) != null;
    }
}
