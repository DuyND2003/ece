package com.example.ece.controller;

import com.example.ece.entity.User;
import com.example.ece.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
     private final UserService userService;

     @GetMapping
     public ResponseEntity<Page<User>> getUsers(
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size) {
         return ResponseEntity.ok( userService.getAllUsers(page, size));
     }

     @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUserById(@RequestParam Long id){
         return ResponseEntity.ok(userService.getUserById(id));
     }

     @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updateUser){
         return ResponseEntity.ok(userService.updateUser(id, updateUser));
     }

     @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
         userService.deleteUser(id);
         return ResponseEntity.ok("User deleted successfully");
     }

     @PatchMapping("/{id}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody String newPassword){
         userService.changePassword(id, newPassword);
         return ResponseEntity.ok("Password changed successfully");
     }
}
