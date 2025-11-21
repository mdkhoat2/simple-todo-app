package com.example.todo.todo;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List todos", description = "List todos for a user; optionally filter by tag")
    public List<Todo> list(@RequestHeader(value = "X-User-Name", required = false) String userName,
                           @RequestHeader(value = "X-User-Id", required = false) String userId,
                           @RequestParam(value = "userName", required = false) String userNameParam,
                           @RequestParam(value = "userId", required = false) String userIdParam,
                           @Parameter(description = "Filter todos by tag", required = false)
                           @RequestParam(value = "tag", required = false) String tag) {
        // Prefer X-User-Name header, then X-User-Id header, then query params. We treat the
        // provided identifier as the user name (no separate numeric id required).
        String uid = userName != null ? userName : (userId != null ? userId : (userNameParam != null ? userNameParam : userIdParam));
        return service.list(uid, tag);
    }

    @PostMapping
    public ResponseEntity<Todo> create(@RequestHeader(value = "X-User-Name", required = false) String userName,
                                       @RequestHeader(value = "X-User-Id", required = false) String userId,
                                       @RequestParam(value = "userName", required = false) String userNameParam,
                                       @RequestParam(value = "userId", required = false) String userIdParam,
                                       @Valid @org.springframework.web.bind.annotation.RequestBody CreateTodoRequest req) {
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

    @PostMapping("/{id}/tags")
    @Operation(summary = "Add tag to todo", description = "Add a single tag to the specified todo")
    public Todo addTag(@RequestHeader(value = "X-User-Name", required = false) String userName,
                       @RequestHeader(value = "X-User-Id", required = false) String userId,
                       @RequestParam(value = "userName", required = false) String userNameParam,
                       @RequestParam(value = "userId", required = false) String userIdParam,
                       @PathVariable long id,
                       @Parameter(description = "Tag to add", required = true)
                       @RequestParam(value = "tag") String tag) {
        String uid = userName != null ? userName : (userId != null ? userId : (userNameParam != null ? userNameParam : userIdParam));
        return service.addTag(uid, id, tag);
    }

    @DeleteMapping("/{id}/tags")
    @Operation(summary = "Remove tag from todo", description = "Remove a single tag from the specified todo")
    public Todo removeTag(@RequestHeader(value = "X-User-Name", required = false) String userName,
                          @RequestHeader(value = "X-User-Id", required = false) String userId,
                          @RequestParam(value = "userName", required = false) String userNameParam,
                          @RequestParam(value = "userId", required = false) String userIdParam,
                          @PathVariable long id,
                          @Parameter(description = "Tag to remove", required = true)
                          @RequestParam(value = "tag") String tag) {
        String uid = userName != null ? userName : (userId != null ? userId : (userNameParam != null ? userNameParam : userIdParam));
        return service.removeTag(uid, id, tag);
    }
}
