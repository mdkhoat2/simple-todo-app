package com.example.todo.todo;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository repository;

    public TodoService(TodoRepository repository) {
        this.repository = repository;
    }

    public List<Todo> list() {
        return repository.findAll();
    }

    public Todo add(String title) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("title must not be blank");
        }
        return repository.save(title.trim());
    }

    public Todo toggle(long id) {
        return repository.toggle(id).orElseThrow(() -> new TodoNotFoundException(id));
    }

    public void delete(long id) {
        boolean removed = repository.delete(id);
        if (!removed) throw new TodoNotFoundException(id);
    }
}
