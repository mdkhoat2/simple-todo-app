package com.example.todo.todo;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Todo JPA entity persisted to PostgreSQL.
 * Immutable API via builder pattern; mutable for JPA hydration.
 */
@Entity
@Table(name = "todos", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id")
})
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean completed;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = true)
    private Instant dueDate;

    @Column(nullable = true)
    private String priority;

    @Column(name = "user_id", nullable = false)
    private String userId;

    // Required by JPA
    public Todo() {
        this.createdAt = Instant.now();
        this.completed = false;
        this.userId = "default";
    }

    public Todo(Long id, String title, boolean completed, Instant createdAt, Instant dueDate, String priority, String userId) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.createdAt = createdAt == null ? Instant.now() : createdAt;
        this.dueDate = dueDate;
        this.priority = priority;
        this.userId = userId == null ? "default" : userId;
    }

    // Legacy constructor for backward compatibility
    public Todo(long id, String title, boolean completed, Instant createdAt) {
        this(id, title, completed, createdAt, null, null, "default");
    }

    public Long getId() {
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

    public String getUserId() {
        return userId;
    }

    public Todo toggle() {
        return new Todo(this.id, this.title, !this.completed, this.createdAt, this.dueDate, this.priority, this.userId);
    }

    public Todo withTitle(String newTitle) {
        return new Todo(this.id, newTitle, this.completed, this.createdAt, this.dueDate, this.priority, this.userId);
    }

    public Todo withCompleted(boolean completed) {
        return new Todo(this.id, this.title, completed, this.createdAt, this.dueDate, this.priority, this.userId);
    }

    public Todo withDueDate(Instant dueDate) {
        return new Todo(this.id, this.title, this.completed, this.createdAt, dueDate, this.priority, this.userId);
    }

    public Todo withPriority(String priority) {
        return new Todo(this.id, this.title, this.completed, this.createdAt, this.dueDate, priority, this.userId);
    }
}
