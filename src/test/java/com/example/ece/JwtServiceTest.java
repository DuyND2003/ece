package com.example.ece;

import com.example.ece.entity.Role;
import com.example.ece.entity.User;
import com.example.ece.service.JwtService;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JwtServiceTest {
    private JwtService jwtService;
    private User user;
    private static final String SECRET_KEY = "213123123123jkhfkfhdfhdhfoshofshdoifhdsiodfdfdsf2342423242";

    @BeforeEach
    void setUp(){
        jwtService = new JwtService();
        user = new User(1L, "testName", "email@gmail.com", "password123", Role.USER);
    }

    @Test
    void testGenerateAcessToken_Success(){
        String token = jwtService.generateAccessToken(user);
        assertNotNull(token);
    }

    @Test
    void testExtractEmail_Success(){
        String token = jwtService.generateAccessToken(user);
        String extractedEmail = jwtService.extractEmail(token);
        assertEquals(user.getEmail(), extractedEmail);
    }
    @Test
    void testGetSigningKey_Success() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        SecretKey signingKey = Keys.hmacShaKeyFor(keyBytes);

        assertNotNull(signingKey);
    }


}
