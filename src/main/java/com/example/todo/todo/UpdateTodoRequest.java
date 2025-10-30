package com.example.todo.todo;

/**
 * DTO for partial updates to a Todo.
 * All fields are optional; the service will apply only present values.
 */
public class UpdateTodoRequest {
    private String title;
    private Boolean completed;
    private String dueDate; // ISO-8601
    private String priority;

    public UpdateTodoRequest() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
