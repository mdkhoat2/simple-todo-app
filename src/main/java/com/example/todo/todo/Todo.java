package com.example.todo.todo;

import java.time.Instant;

/**
 * Todo model exposed by the API. Kept immutable for thread-safety in the in-memory
 * repository. Added optional dueDate and priority fields to match frontend shape.
 */

public class Todo {
    private final long id;
    private final String title;
    private final boolean completed;
    private final Instant createdAt;
    private final Instant dueDate;
    private final String priority;

    public Todo(long id, String title, boolean completed, Instant createdAt) {
        this(id, title, completed, createdAt, null, null);
    }

    public Todo(long id, String title, boolean completed, Instant createdAt, Instant dueDate, String priority) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.createdAt = createdAt == null ? Instant.now() : createdAt;
        this.dueDate = dueDate;
        this.priority = priority;
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

    public Instant getDueDate() {
        return dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public Todo toggle() {
        return new Todo(this.id, this.title, !this.completed, this.createdAt, this.dueDate, this.priority);
    }

    public Todo withTitle(String newTitle) {
        return new Todo(this.id, newTitle, this.completed, this.createdAt, this.dueDate, this.priority);
    }

    public Todo withCompleted(boolean completed) {
        return new Todo(this.id, this.title, completed, this.createdAt, this.dueDate, this.priority);
    }

    public Todo withDueDate(Instant dueDate) {
        return new Todo(this.id, this.title, this.completed, this.createdAt, dueDate, this.priority);
    }

    public Todo withPriority(String priority) {
        return new Todo(this.id, this.title, this.completed, this.createdAt, this.dueDate, priority);
    }
}
