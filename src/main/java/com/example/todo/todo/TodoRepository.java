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
    private final ConcurrentMap<Long, Todo> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    public Todo save(String title) {
        long id = seq.incrementAndGet();
        Todo todo = new Todo(id, title, false, Instant.now());
        store.put(id, todo);
        return todo;
    }

    public List<Todo> findAll() {
        Collection<Todo> values = store.values();
        List<Todo> list = new ArrayList<>(values);
        list.sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
        return Collections.unmodifiableList(list);
    }

    public Optional<Todo> findById(long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Optional<Todo> toggle(long id) {
        return Optional.ofNullable(store.computeIfPresent(id, (k, v) -> v.toggle()));
    }

    public boolean delete(long id) {
        return store.remove(id) != null;
    }

    public void clear() {
        store.clear();
        seq.set(0);
    }
}
