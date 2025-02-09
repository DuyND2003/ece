package com.example.ece;

import com.example.ece.entity.Role;
import com.example.ece.entity.User;
import com.example.ece.repository.UserRepository;
import com.example.ece.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UnitServiceTest {

    // gia lap mock tranh goi database that
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    // Inject cac mock vao user service
    @InjectMocks
    private UserService userService;

    private User user;

    // Chuan bi du lieu fake
    @BeforeEach
    void setUp() {
        user = new User(1L, "testUser", "test@gmail.com", "rewrwereew", Role.USER);
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(1L);

        // Kiem tra ket qua
        assertTrue(foundUser.isPresent());
        assertEquals("test@gmail.com", foundUser.get().getEmail());
    }

    @Test
    void testDeleteUser_Success() {
        doNothing().when(userRepository).deleteById(1L);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
