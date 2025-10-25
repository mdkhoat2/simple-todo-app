package com.example.todo.todo;

import java.time.Instant;

public class Todo {
    private final long id;
    private final String title;
    private final boolean completed;
    private final Instant createdAt;

    public Todo(long id, String title, boolean completed, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.createdAt = createdAt == null ? Instant.now() : createdAt;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Todo toggle() {
        return new Todo(this.id, this.title, !this.completed, this.createdAt);
    }
}
