package com.example.todo.todo;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Todo> list(@RequestHeader(value = "X-User-Name", required = false) String userName,
                           @RequestHeader(value = "X-User-Id", required = false) String userId,
                           @RequestParam(value = "userName", required = false) String userNameParam,
                           @RequestParam(value = "userId", required = false) String userIdParam) {
        // Prefer X-User-Name header, then X-User-Id header, then query params. We treat the
        // provided identifier as the user name (no separate numeric id required).
        String uid = userName != null ? userName : (userId != null ? userId : (userNameParam != null ? userNameParam : userIdParam));
        return service.list(uid);
    }

    @PostMapping
    public ResponseEntity<Todo> create(@RequestHeader(value = "X-User-Name", required = false) String userName,
                                       @RequestHeader(value = "X-User-Id", required = false) String userId,
                                       @RequestParam(value = "userName", required = false) String userNameParam,
                                       @RequestParam(value = "userId", required = false) String userIdParam,
                                       @Valid @RequestBody CreateTodoRequest req) {
        String uid = userName != null ? userName : (userId != null ? userId : (userNameParam != null ? userNameParam : userIdParam));
        Todo created = service.add(uid, req);
        return ResponseEntity.created(URI.create("/api/todos/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public Todo update(@RequestHeader(value = "X-User-Name", required = false) String userName,
                       @RequestHeader(value = "X-User-Id", required = false) String userId,
                       @RequestParam(value = "userName", required = false) String userNameParam,
                       @RequestParam(value = "userId", required = false) String userIdParam,
                       @PathVariable long id, @RequestBody UpdateTodoRequest req) {
        String uid = userName != null ? userName : (userId != null ? userId : (userNameParam != null ? userNameParam : userIdParam));
        return service.update(uid, id, req);
    }

    @PutMapping("/{id}/toggle")
    public Todo toggle(@RequestHeader(value = "X-User-Name", required = false) String userName,
                       @RequestHeader(value = "X-User-Id", required = false) String userId,
                       @RequestParam(value = "userName", required = false) String userNameParam,
                       @RequestParam(value = "userId", required = false) String userIdParam,
                       @PathVariable long id) {
        String uid = userName != null ? userName : (userId != null ? userId : (userNameParam != null ? userNameParam : userIdParam));
        return service.toggle(uid, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader(value = "X-User-Name", required = false) String userName,
                                       @RequestHeader(value = "X-User-Id", required = false) String userId,
                                       @RequestParam(value = "userName", required = false) String userNameParam,
                                       @RequestParam(value = "userId", required = false) String userIdParam,
                                       @PathVariable long id) {
        String uid = userName != null ? userName : (userId != null ? userId : (userNameParam != null ? userNameParam : userIdParam));
        service.delete(uid, id);
        return ResponseEntity.noContent().build();
    }
}
