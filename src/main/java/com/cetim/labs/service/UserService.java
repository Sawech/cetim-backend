package com.cetim.labs.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cetim.labs.model.User;
import com.cetim.labs.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getUsersByRole(String roleName) {
        return userRepository.findAll().stream()
            .filter(user -> user.getClass().getSimpleName().equalsIgnoreCase(roleName))
            .collect(Collectors.toList());
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setphonenumber(userDetails.getphonenumber());
        user.setSousDirection(userDetails.getSousDirection());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getUsersByService(String serviceName) {
        List<User> usir = userRepository.findAll().stream()
            .filter(user -> user.getService() != null && user.getService().equalsIgnoreCase("chimique"))
            .collect(Collectors.toList());

        if (usir.isEmpty()) {
            throw new RuntimeException("No users found for service: " + serviceName);
        }   

        return usir;
    }
}