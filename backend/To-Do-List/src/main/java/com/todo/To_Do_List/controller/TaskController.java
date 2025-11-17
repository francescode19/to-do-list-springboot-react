package com.todo.To_Do_List.controller;

import com.todo.To_Do_List.model.Task;
import com.todo.To_Do_List.model.User;
import com.todo.To_Do_List.service.TaskService;
import com.todo.To_Do_List.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class TaskController {

    private final TaskService service;
    private final UserRepository userRepository;

    @Autowired
    public TaskController(TaskService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @GetMapping("/tasks")
    public List<Task> all(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utente non trovato."));
        return service.getTasksByUser(user);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> one(@PathVariable Long id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utente non trovato."));

        return service.getTaskByIdAndUser(id, user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/tasks")
    public void create(@RequestBody Task t, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utente non trovato."));
        service.createTaskForUser(t, user);
    }

    @PutMapping("/tasks/{id}")
    public Task update(@PathVariable Long id, @RequestBody Task t, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utente non trovato."));
        return service.updateTaskForUser(id, t, user);
    }

    @DeleteMapping("/tasks/{id}")
    public void delete(@PathVariable Long id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utente non trovato."));
        service.deleteTaskByIdForUser(id, user);
    }
}
