package com.todo.To_Do_List.service;

import com.todo.To_Do_List.model.Task;
import com.todo.To_Do_List.model.User;
import com.todo.To_Do_List.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository repo;

    @Autowired
    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public List<Task> getAllTasks() {
        return repo.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return repo.findById(id);
    }

    public void createTask(Task task) {
        repo.save(task);
    }

    public Task updateTask(Long id, Task task) {
        boolean exists = repo.existsById(id);
        if (exists) {
            return repo.save(task);
        } else {
            return null;
        }
    }

    public void deleteTaskById(Long id) {
        repo.deleteById(id);
    }



    public List<Task> getTasksByUser(User user) {
        return repo.findByOwner(user);
    }

    public Optional<Task> getTaskByIdAndUser(Long id, User user) {
        return repo.findByIdAndOwner(id, user);
    }

    public void createTaskForUser(Task task, User user) {
        task.setOwner(user);
        repo.save(task);
    }

    public Task updateTaskForUser(Long id, Task updatedTask, User user) {
        return repo.findByIdAndOwner(id, user).map(task -> {
            task.setName(updatedTask.getName());
            task.setDescription(updatedTask.getDescription());
            task.setCompleted(updatedTask.isCompleted());
            return repo.save(task);
        }).orElseThrow(() -> new RuntimeException("Task non trovato o non autorizzato."));
    }

    public void deleteTaskByIdForUser(Long id, User user) {
        repo.findByIdAndOwner(id, user).ifPresentOrElse(
                repo::delete,
                () -> { throw new RuntimeException("Task non trovato o non autorizzato."); }
        );
    }
}
