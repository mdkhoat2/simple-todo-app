package com.example.todo.todo;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class TodoService {
    private final TodoRepository repository;

    public TodoService(TodoRepository repository) {
        this.repository = repository;
    }

    // List todos for a specific user (userName may be null to use the default/global store)
    public List<Todo> list(String userName) {
        return repository.findAllForUser(userName);
    }

    // Backwards-compatible list()
    public List<Todo> list() {
        return list(null);
    }

    public Todo add(String title) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("title must not be blank");
        }
        return repository.save(title.trim());
    }

    public Todo add(CreateTodoRequest req) {
        String title = req.getTitle();
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("title must not be blank");
        }
        Instant due = parseDueDate(req.getDueDate());
        String priority = req.getPriority();
        return repository.save(title.trim(), due, priority);
    }
    // Add for specific user (if userName is null default store is used)
    public Todo add(String userName, CreateTodoRequest req) {
        String title = req.getTitle();
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("title must not be blank");
        }
        Instant due = parseDueDate(req.getDueDate());
        String priority = req.getPriority();
        return repository.saveForUser(userName, title.trim(), due, priority);
    }

    public Todo toggle(String userName, long id) {
        return repository.toggleForUser(userName, id).orElseThrow(() -> new TodoNotFoundException(id));
    }

    public Todo update(String userName, long id, UpdateTodoRequest req) {
        Todo existing = repository.findByIdForUser(userName, id).orElseThrow(() -> new TodoNotFoundException(id));

        String newTitle = req.getTitle() != null ? req.getTitle().trim() : existing.getTitle();
        boolean newCompleted = req.getCompleted() != null ? req.getCompleted() : existing.isCompleted();

        Instant newDue = existing.getDueDate();
        if (req.getDueDate() != null) {
            Instant parsed = parseDueDate(req.getDueDate());
            newDue = parsed != null ? parsed : existing.getDueDate();
        }

        String newPriority = req.getPriority() != null ? req.getPriority() : existing.getPriority();

        Todo updated = new Todo(existing.getId(), newTitle, newCompleted, existing.getCreatedAt(), newDue, newPriority);
        return repository.updateForUser(userName, updated).orElseThrow(() -> new TodoNotFoundException(id));
    }

    // Helper to parse a dueDate string sent by the frontend.
    // Accepts full ISO instants (e.g. 2025-10-30T10:00:00Z) or date-only (YYYY-MM-DD).
    // For date-only we convert to start-of-day in system default zone.
    private Instant parseDueDate(String s) {
        if (s == null) return null;
        try {
            return Instant.parse(s);
        } catch (Exception e) {
            // try LocalDate
        }
        try {
            LocalDate ld = LocalDate.parse(s);
            return ld.atStartOfDay(ZoneId.systemDefault()).toInstant();
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(String userName, long id) {
        boolean removed = repository.deleteForUser(userName, id);
        if (!removed) throw new TodoNotFoundException(id);
    }

    // Backwards-compatible wrappers (no userId)
    public Todo add(String title, Instant dueDate, String priority) {
        return repository.save(title, dueDate, priority);
    }

    public Todo toggle(long id) {
        return toggle(null, id);
    }

    public Todo update(long id, UpdateTodoRequest req) {
        return update(null, id, req);
    }

    public void delete(long id) {
        delete(null, id);
    }
}
