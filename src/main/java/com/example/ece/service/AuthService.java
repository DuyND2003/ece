package com.example.ece.service;

import com.example.ece.dto.LoginRequest;
import com.example.ece.dto.RefreshTokenRequest;
import com.example.ece.entity.RefreshToken;
import com.example.ece.entity.Role; // ĐẢM BẢO IMPORT ĐÚNG
import com.example.ece.dto.AuthResponse;
import com.example.ece.dto.RegisterRequest;
import com.example.ece.entity.User;
import com.example.ece.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Quan ly authen , dang nhap, dang ky, lam moi token
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    /**
     * Xử lý đăng ký tài khoản mới.
     * @param registerRequest Dữ liệu đăng ký từ người dùng.
     * @return AuthResponse chứa Access Token và Refresh Token.
     */
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

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    /**
     * Xử lý đăng nhập.
     * @param loginRequest Dữ liệu đăng nhập từ người dùng.
     * @return AuthResponse chứa Access Token và Refresh Token.
     */
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
        throw new RuntimeException("Invalid email or password");}

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    /**
     * Làm mới Access Token bằng Refresh Token.
     * @param request Yêu cầu làm mới token từ client.
     * @return AuthResponse chứa Access Token mới và Refresh Token hiện tại.
     */
    public AuthResponse refreshToken(RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if(!refreshTokenService.validateRefreshToken(refreshToken)) {
            refreshTokenService.deleteByToken( refreshToken.getToken());
            throw new RuntimeException("Refresh token expired");
        }

        String accessToken = jwtService.generateAccessToken(refreshToken.getUser());
        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    /**
     * Đăng xuất người dùng bằng cách xóa Refresh Token của họ.
     * @param refreshToken Refresh Token cần xóa.
     */
    public void logout(String refreshToken) {
        refreshTokenService.findByToken(refreshToken)
                .ifPresent(token -> refreshTokenService.deleteByToken(refreshToken));
    }
}
