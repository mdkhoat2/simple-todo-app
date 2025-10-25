package com.example.todo.todo;

import jakarta.validation.constraints.NotBlank;

public class CreateTodoRequest {
    @NotBlank
    private String title;

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
}
