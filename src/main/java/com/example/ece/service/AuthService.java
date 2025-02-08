package com.example.ece.service;

import com.example.ece.dto.LoginRequest;
import com.example.ece.entity.Role; // ĐẢM BẢO IMPORT ĐÚNG
import com.example.ece.dto.AuthResponse;
import com.example.ece.dto.RegisterRequest;
import com.example.ece.entity.User;
import com.example.ece.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest registerRequest) {
        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already taken!"); // THÊM DẤU ;
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        // Tạo JWT token và trả về
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
        throw new RuntimeException("Invalid email or password");}
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
