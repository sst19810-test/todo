package com.example.todo.controller;

import com.example.todo.model.ApiResponse;
import com.example.todo.model.Todo;
import com.example.todo.model.TodoRequest;
import com.example.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    // ── POST /api/todos ──────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<ApiResponse<Todo>> create(@Valid @RequestBody TodoRequest request) {
        Todo created = todoService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Todo created successfully", created));
    }

    // ── GET /api/todos ───────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<Todo>>> getAll(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) Todo.Priority priority,
            @RequestParam(required = false) String search) {

        List<Todo> todos;

        if (search != null && !search.isBlank()) {
            todos = todoService.search(search);
        } else if (completed != null) {
            todos = todoService.getByCompleted(completed);
        } else if (priority != null) {
            todos = todoService.getByPriority(priority);
        } else {
            todos = todoService.getAll();
        }

        return ResponseEntity.ok(ApiResponse.success(todos));
    }

    // ── GET /api/todos/{id} ──────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Todo>> getById(@PathVariable Long id) {
        Todo todo = todoService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(todo));
    }

    // ── PUT /api/todos/{id} ──────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Todo>> update(
            @PathVariable Long id,
            @Valid @RequestBody TodoRequest request) {
        Todo updated = todoService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Todo updated successfully", updated));
    }

    // ── PATCH /api/todos/{id}/toggle ─────────────────────────────────────────
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<Todo>> toggle(@PathVariable Long id) {
        Todo toggled = todoService.toggleComplete(id);
        String msg = toggled.isCompleted() ? "Todo marked as complete" : "Todo marked as incomplete";
        return ResponseEntity.ok(ApiResponse.success(msg, toggled));
    }

    // ── DELETE /api/todos/{id} ───────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        todoService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Todo deleted successfully", null));
    }

    // ── DELETE /api/todos/completed ──────────────────────────────────────────
    @DeleteMapping("/completed")
    public ResponseEntity<ApiResponse<Void>> deleteAllCompleted() {
        todoService.deleteAllCompleted();
        return ResponseEntity.ok(ApiResponse.success("All completed todos deleted", null));
    }

    // ── GET /api/todos/stats ─────────────────────────────────────────────────
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        Map<String, Object> stats = todoService.getStats();
        return ResponseEntity.ok(ApiResponse.success("Stats retrieved", stats));
    }
}
