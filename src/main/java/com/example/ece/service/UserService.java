package com.example.ece.service;

import com.example.ece.entity.User;
import com.example.ece.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<User> getAllUsers(int page, int size){
        return userRepository.findAll(PageRequest.of(page, size));
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User updateUser){
        User user =  userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(updateUser.getUsername());
        user.setEmail(updateUser.getEmail());
        return userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public void changePassword(Long id, String newPassword){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
