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
        return saveForUser(null, title, null, null, null);
    }

    public Todo save(String title, Instant dueDate, String priority) {
        return saveForUser(null, title, dueDate, priority, null);
    }

    public Todo save(String title, Instant dueDate, String priority, java.util.List<String> tags) {
        return saveForUser(null, title, dueDate, priority, tags);
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
    public Todo saveForUser(String userId, String title, Instant dueDate, String priority, java.util.List<String> tags) {
        String uid = userId == null ? DEFAULT_USER : userId;
        Todo todo = new Todo(null, title, false, Instant.now(), dueDate, priority, uid, tags);
        return jpaRepository.save(todo);
    }

    public Todo saveForUser(String userId, String title, Instant dueDate, String priority) {
        return saveForUser(userId, title, dueDate, priority, null);
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

    public Optional<Todo> addTagForUser(String userId, long id, String tag) {
        String uid = userId == null ? DEFAULT_USER : userId;
        Optional<Todo> existing = jpaRepository.findByIdAndUserId(id, uid);
        if (existing.isPresent()) {
            Todo t = existing.get();
            java.util.List<String> tags = t.getTags();
            if (tags == null) tags = new java.util.ArrayList<>();
            if (!tags.contains(tag)) tags.add(tag);
            Todo updated = t.withTags(tags);
            return Optional.of(jpaRepository.save(updated));
        }
        return Optional.empty();
    }

    public Optional<Todo> removeTagForUser(String userId, long id, String tag) {
        String uid = userId == null ? DEFAULT_USER : userId;
        Optional<Todo> existing = jpaRepository.findByIdAndUserId(id, uid);
        if (existing.isPresent()) {
            Todo t = existing.get();
            java.util.List<String> tags = t.getTags();
            if (tags == null || !tags.contains(tag)) return Optional.of(t);
            java.util.List<String> newTags = new java.util.ArrayList<>(tags);
            newTags.remove(tag);
            Todo updated = t.withTags(newTags);
            return Optional.of(jpaRepository.save(updated));
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
