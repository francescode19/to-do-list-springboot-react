package com.todo.To_Do_List.repository;

import com.todo.To_Do_List.model.Task;
import com.todo.To_Do_List.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository <Task, Long> {
    List<Task> findByOwner(User owner);
    Optional<Task> findByIdAndOwner(Long id, User owner);
}
