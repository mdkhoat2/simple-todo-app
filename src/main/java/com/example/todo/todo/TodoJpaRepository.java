package com.example.todo.todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for Todo entities.
 * Provides database persistence via PostgreSQL.
 */
@Repository
public interface TodoJpaRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserId(String userId);
    Optional<Todo> findByIdAndUserId(Long id, String userId);
    void deleteByIdAndUserId(Long id, String userId);
}
