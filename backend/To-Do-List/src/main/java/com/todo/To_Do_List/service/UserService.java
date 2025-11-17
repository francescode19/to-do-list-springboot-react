package com.todo.To_Do_List.service;

import com.todo.To_Do_List.model.User;
import com.todo.To_Do_List.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public Optional<User> getUser(String username) {
        return repo.findByUsername(username);
    }

    public void createUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
    }

    public User updateUser(Long id, User user) {
        boolean exists = repo.existsById(id);
        if (exists) {
            user.setId(id);
            return repo.save(user);
        } else {
            return null;
        }
    }

    public void deleteUserById(Long id) {
        repo.deleteById(id);
    }

    public User findByUsername(String username) {
        return repo.findByUsername(username).orElse(null);
    }

}
