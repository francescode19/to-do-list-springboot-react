package com.todo.To_Do_List.repository;

import com.todo.To_Do_List.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    Optional<User> findByUsername(String username);
}
