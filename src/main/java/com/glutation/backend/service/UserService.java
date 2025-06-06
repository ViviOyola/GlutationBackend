package com.glutation.backend.service;

import com.glutation.backend.entity.User;
import com.glutation.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        // Add any validation or business logic before saving
        return userRepository.save(user);
    }

    public Optional<User> updateUser(Integer id, User userDetails) {
        return userRepository.findById(id)
            .map(user -> {
                user.setEmail(userDetails.getEmail());
                user.setPassword(userDetails.getPassword()); // Consider password encoding
                user.setAddress(userDetails.getAddress());
                user.setName(userDetails.getName());
                user.setTelefonoContacto(userDetails.getTelefonoContacto());
                return userRepository.save(user);
            });
    }

    public boolean deleteUser(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<User> getUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
} 