package com.example.todo.todo;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * In-memory mock for TodoJpaRepository used in unit tests.
 */
public class InMemoryTodoJpaRepository implements TodoJpaRepository {
    private final ConcurrentHashMap<Long, Todo> store = new ConcurrentHashMap<>();
    private long nextId = 0;

    @Override
    public <S extends Todo> S save(S entity) {
        if (entity.getId() == null) {
            try {
                var f = Todo.class.getDeclaredField("id");
                f.setAccessible(true);
                f.set(entity, ++nextId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public List<Todo> findByUserId(String userId) {
        return store.values().stream()
                .filter(t -> t.getUserId().equals(userId))
                .sorted(Comparator.comparing(Todo::getCreatedAt))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Todo> findByIdAndUserId(Long id, String userId) {
        Todo t = store.get(id);
        if (t != null && t.getUserId().equals(userId)) {
            return Optional.of(t);
        }
        return Optional.empty();
    }

    @Override
    public void deleteByIdAndUserId(Long id, String userId) {
        Todo t = store.get(id);
        if (t != null && t.getUserId().equals(userId)) {
            store.remove(id);
        }
    }

    @Override
    public void delete(Todo entity) {
        store.remove(entity.getId());
    }

    @Override
    public void deleteAll(Iterable<? extends Todo> entities) {
        for (Todo e : entities) {
            store.remove(e.getId());
        }
    }

    // JpaRepository CRUD methods
    @Override
    public <S extends Todo> List<S> saveAll(Iterable<S> entities) { return new ArrayList<>(); }
    
    @Override
    public Optional<Todo> findById(Long id) { return Optional.empty(); }
    
    @Override
    public boolean existsById(Long id) { return false; }
    
    @Override
    public List<Todo> findAll() { return new ArrayList<>(); }
    
    @Override
    public List<Todo> findAllById(Iterable<Long> ids) { return new ArrayList<>(); }
    
    @Override
    public long count() { return 0; }
    
    @Override
    public void deleteById(Long id) {}
    
    @Override
    public void deleteAll() {}
    
    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {}
    
    @Override
    public void flush() {}
    
    @Override
    public <S extends Todo> S saveAndFlush(S entity) { return null; }
    
    @Override
    public <S extends Todo> List<S> saveAllAndFlush(Iterable<S> entities) { return new ArrayList<>(); }
    
    @Override
    public void deleteInBatch(Iterable<Todo> entities) {}
    
    @Override
    public void deleteAllInBatch() {}
    
    @Override
    public void deleteAllInBatch(Iterable<Todo> entities) {}
    
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {}
    
    @Override
    public Todo getOne(Long id) { return null; }
    
    @Override
    public Todo getById(Long id) { return null; }
    
    @Override
    public Todo getReferenceById(Long id) { return null; }
    
    // PagingAndSortingRepository methods
    @Override
    public List<Todo> findAll(Sort sort) { return new ArrayList<>(); }
    
    @Override
    public Page<Todo> findAll(Pageable pageable) { return null; }
    
    // QueryByExampleExecutor methods
    @Override
    public <S extends Todo> Optional<S> findOne(Example<S> example) { return Optional.empty(); }
    
    @Override
    public <S extends Todo> List<S> findAll(Example<S> example) { return new ArrayList<>(); }
    
    @Override
    public <S extends Todo> List<S> findAll(Example<S> example, Sort sort) { return new ArrayList<>(); }
    
    @Override
    public <S extends Todo> Page<S> findAll(Example<S> example, Pageable pageable) { return null; }
    
    @Override
    public <S extends Todo> long count(Example<S> example) { return 0; }
    
    @Override
    public <S extends Todo> boolean exists(Example<S> example) { return false; }
    
    @Override
    public <S extends Todo, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) { return null; }
}
