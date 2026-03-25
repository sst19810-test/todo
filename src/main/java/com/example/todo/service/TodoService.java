package com.example.todo.service;

import com.example.todo.exception.TodoNotFoundException;
import com.example.todo.model.Todo;
import com.example.todo.model.TodoRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class TodoService {
    // In-memory store using ConcurrentHashMap for thread safety
    private final Map<Long, Todo> todoStore = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);
// private final AtomicLong idCounter = new AtomicLong(1);
    public TodoService() {
        // Seed with some sample data
        createSampleData();
    }

    // ── CREATE ──────────────────────────────────────────────────────────────

    public Todo create(TodoRequest request) {
        Todo todo = Todo.builder()
                .id(idCounter.getAndIncrement())
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(request.isCompleted())
                .priority(request.getPriority() != null ? request.getPriority() : Todo.Priority.MEDIUM)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        todoStore.put(todo.getId(), todo);
        return todo;
    }

    // ── READ ────────────────────────────────────────────────────────────────

    public List<Todo> getAll() {
        return todoStore.values().stream()
                .sorted(Comparator.comparing(Todo::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public Todo getById(Long id) {
        Todo todo = todoStore.get(id);
        if (todo == null) {
            throw new TodoNotFoundException(id);
        }
        return todo;
    }

    public List<Todo> getByCompleted(boolean completed) {
        return todoStore.values().stream()
                .filter(t -> t.isCompleted() == completed)
                .sorted(Comparator.comparing(Todo::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public List<Todo> getByPriority(Todo.Priority priority) {
        return todoStore.values().stream()
                .filter(t -> t.getPriority() == priority)
                .sorted(Comparator.comparing(Todo::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public List<Todo> search(String keyword) {
        String lower = keyword.toLowerCase();
        return todoStore.values().stream()
                .filter(t -> t.getTitle().toLowerCase().contains(lower)
                        || (t.getDescription() != null && t.getDescription().toLowerCase().contains(lower)))
                .sorted(Comparator.comparing(Todo::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    // ── UPDATE ──────────────────────────────────────────────────────────────

    public Todo update(Long id, TodoRequest request) {
        Todo existing = getById(id);

        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        existing.setCompleted(request.isCompleted());
        existing.setPriority(request.getPriority() != null ? request.getPriority() : existing.getPriority());
        existing.setUpdatedAt(LocalDateTime.now());

        todoStore.put(id, existing);
        return existing;
    }

    public Todo toggleComplete(Long id) {
        Todo todo = getById(id);
        todo.setCompleted(!todo.isCompleted());
        todo.setUpdatedAt(LocalDateTime.now());
        todoStore.put(id, todo);
        return todo;
    }

    // ── DELETE ──────────────────────────────────────────────────────────────

    public void delete(Long id) {
        if (!todoStore.containsKey(id)) {
            throw new TodoNotFoundException(id);
        }
        todoStore.remove(id);
    }

    public void deleteAllCompleted() {
        todoStore.entrySet().removeIf(entry -> entry.getValue().isCompleted());
    }

    // ── STATS ───────────────────────────────────────────────────────────────

    public Map<String, Object> getStats() {
        long total = todoStore.size();
        long completed = todoStore.values().stream().filter(Todo::isCompleted).count();
        long pending = total - completed;

        Map<String, Long> byPriority = new LinkedHashMap<>();
        for (Todo.Priority p : Todo.Priority.values()) {
            byPriority.put(p.name(), todoStore.values().stream()
                    .filter(t -> t.getPriority() == p).count());
        }

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("total", total);
        stats.put("completed", completed);
        stats.put("pending", pending);
        stats.put("byPriority", byPriority);
        return stats;
    }

    // ── SEED DATA ───────────────────────────────────────────────────────────

    private void createSampleData() {
        List<TodoRequest> samples = new ArrayList<>();

        TodoRequest t1 = new TodoRequest();
        t1.setTitle("Buy groceries");
        t1.setDescription("Milk, eggs, bread, and coffee");
        t1.setPriority(Todo.Priority.HIGH);
        samples.add(t1);

        TodoRequest t2 = new TodoRequest();
        t2.setTitle("Read Clean Code book");
        t2.setDescription("Finish chapters 5 through 8");
        t2.setPriority(Todo.Priority.MEDIUM);
        samples.add(t2);

        TodoRequest t3 = new TodoRequest();
        t3.setTitle("Go for a morning run");
        t3.setDescription("30 minutes around the park");
        t3.setPriority(Todo.Priority.LOW);
        t3.setCompleted(true);
        samples.add(t3);

        TodoRequest t4 = new TodoRequest();
        t4.setTitle("Fix login bug");
        t4.setDescription("Users unable to reset password via email link");
        t4.setPriority(Todo.Priority.HIGH);
        samples.add(t4);

        TodoRequest t5 = new TodoRequest();
        t5.setTitle("Plan team lunch");
        t5.setDescription("Book a restaurant for Friday");
        t5.setPriority(Todo.Priority.LOW);
        samples.add(t5);

        samples.forEach(this::create);
    }
}
