package com.example.todo.todo;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public class CreateTodoRequest {
    @NotBlank
    private String title;

    // optional fields supported by frontend
    private String dueDate; // ISO-8601 string, optional
    private String priority; // e.g. "low" | "medium" | "high"
    @Schema(description = "Optional tags for this todo")
    private List<String> tags; // optional tags, e.g. ["home","shopping"]

    public CreateTodoRequest() {}

    public CreateTodoRequest(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public java.util.List<String> getTags() {
        return tags;
    }

    public void setTags(java.util.List<String> tags) {
        this.tags = tags;
    }
}
