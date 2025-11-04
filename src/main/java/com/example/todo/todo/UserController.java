package com.example.todo.todo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
 

/**
 * Minimal users endpoint to satisfy the frontend createUser call.
 * This is intentionally simple — it echoes the created user back with 201.
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @PostMapping
    public ResponseEntity<Map<String, String>> createUser(@RequestBody Map<String, String> body) {
        String name = body.getOrDefault("name", "");
        // For simplicity we treat the user name as the identifier. Return the name so
        // the frontend can store it; no generated UUID is required per user's request.
        return ResponseEntity.created(URI.create("/api/users/" + name)).body(Map.of("name", name));
    }
}
