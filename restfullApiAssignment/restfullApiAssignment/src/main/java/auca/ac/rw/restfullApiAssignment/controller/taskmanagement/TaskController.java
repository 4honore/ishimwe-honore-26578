package auca.ac.rw.restfullApiAssignment.controller.taskmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import auca.ac.rw.restfullApiAssignment.modal.taskmanagement.Task;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final List<Task> tasks = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public TaskController() {
        tasks.add(new Task(idGen.getAndIncrement(), "Buy groceries", "Milk, eggs, bread", false, "MEDIUM", "2026-02-10"));
        tasks.add(new Task(idGen.getAndIncrement(), "Finish assignment", "Complete REST API tasks", true, "HIGH", "2026-02-07"));
        tasks.add(new Task(idGen.getAndIncrement(), "Read book", "Read Clean Code", false, "LOW", "2026-03-01"));
    }

    @GetMapping
    public List<Task> getAll() {
        return tasks;
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getById(@PathVariable Long taskId) {
        Optional<Task> t = tasks.stream().filter(x -> x.getTaskId().equals(taskId)).findFirst();
        return t.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/status")
    public List<Task> byStatus(@RequestParam boolean completed) {
        return tasks.stream().filter(t -> t.isCompleted() == completed).collect(Collectors.toList());
    }

    @GetMapping("/priority/{priority}")
    public List<Task> byPriority(@PathVariable String priority) {
        String p = priority.toUpperCase();
        return tasks.stream().filter(t -> t.getPriority().equalsIgnoreCase(p)).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Task> create(@RequestBody Task task) {
        task.setTaskId(idGen.getAndIncrement());
        tasks.add(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> update(@PathVariable Long taskId, @RequestBody Task updated) {
        Optional<Task> t = tasks.stream().filter(x -> x.getTaskId().equals(taskId)).findFirst();
        if (t.isPresent()) {
            Task tt = t.get();
            tt.setTitle(updated.getTitle());
            tt.setDescription(updated.getDescription());
            tt.setCompleted(updated.isCompleted());
            tt.setPriority(updated.getPriority());
            tt.setDueDate(updated.getDueDate());
            return ResponseEntity.ok(tt);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<Task> markCompleted(@PathVariable Long taskId) {
        Optional<Task> t = tasks.stream().filter(x -> x.getTaskId().equals(taskId)).findFirst();
        if (t.isPresent()) {
            Task tt = t.get();
            tt.setCompleted(true);
            return ResponseEntity.ok(tt);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> delete(@PathVariable Long taskId) {
        boolean removed = tasks.removeIf(t -> t.getTaskId().equals(taskId));
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
