package com.example.ece.service;

import com.example.ece.entity.RefreshToken;
import com.example.ece.entity.User;
import com.example.ece.repository.RefreshTokenRepository;
import com.example.ece.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Service quản lý Refresh Token.
 * - Tạo mới refresh token khi đăng nhập hoặc làm mới phiên làm việc.
 * - Xác thực refresh token (hết hạn hay chưa).
 * - Xóa refresh token của người dùng khi cần.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60;

    public RefreshToken createRefreshToken (User user ){
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRATION))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    // Tim refresh token
    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    // Kiem tra refresh token co hop le
    public boolean validateRefreshToken(RefreshToken refreshToken){
        return refreshToken.getExpiryDate().isAfter(Instant.now());
    }

    // Xoa token cua user ( vi du khi dang xuat)
    public void deleteByToken(String token){
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }
}
