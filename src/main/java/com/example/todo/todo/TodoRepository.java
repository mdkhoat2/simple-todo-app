package com.example.todo.todo;

import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * TodoRepository bridges business logic with JPA persistence.
 * Per-user scoping is maintained; default user is used for backward compatibility.
 */
@Repository
public class TodoRepository {
    private static final String DEFAULT_USER = "__default__";

    private final TodoJpaRepository jpaRepository;

    public TodoRepository(TodoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
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
        List<Todo> defaults = jpaRepository.findByUserId(DEFAULT_USER);
        jpaRepository.deleteAll(defaults);
    }

    // Per-user API
    public Todo saveForUser(String userId, String title, Instant dueDate, String priority) {
        String uid = userId == null ? DEFAULT_USER : userId;
        Todo todo = new Todo(null, title, false, Instant.now(), dueDate, priority, uid);
        return jpaRepository.save(todo);
    }

    public List<Todo> findAllForUser(String userId) {
        String uid = userId == null ? DEFAULT_USER : userId;
        List<Todo> list = new ArrayList<>(jpaRepository.findByUserId(uid));
        list.sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
        return list;
    }

    public Optional<Todo> findByIdForUser(String userId, long id) {
        String uid = userId == null ? DEFAULT_USER : userId;
        return jpaRepository.findByIdAndUserId(id, uid);
    }

    public Optional<Todo> toggleForUser(String userId, long id) {
        String uid = userId == null ? DEFAULT_USER : userId;
        Optional<Todo> opt = jpaRepository.findByIdAndUserId(id, uid);
        if (opt.isPresent()) {
            Todo toggled = opt.get().toggle();
            return Optional.of(jpaRepository.save(toggled));
        }
        return Optional.empty();
    }

    public Optional<Todo> updateForUser(String userId, Todo todo) {
        String uid = userId == null ? DEFAULT_USER : userId;
        Optional<Todo> existing = jpaRepository.findByIdAndUserId(todo.getId(), uid);
        if (existing.isPresent()) {
            return Optional.of(jpaRepository.save(todo));
        }
        return Optional.empty();
    }

    public boolean deleteForUser(String userId, long id) {
        String uid = userId == null ? DEFAULT_USER : userId;
        Optional<Todo> existing = jpaRepository.findByIdAndUserId(id, uid);
        if (existing.isPresent()) {
            jpaRepository.delete(existing.get());
            return true;
        }
        return false;
    }
}
