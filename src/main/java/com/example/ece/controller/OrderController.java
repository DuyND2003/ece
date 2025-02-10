package com.example.ece.controller;

import com.example.ece.entity.Order;
import com.example.ece.entity.OrderStatus;
import com.example.ece.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // Tạo đơn hàng từ giỏ hàng
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestHeader("Authorization") String token) {
        String email = extractEmailFromToken(token);
        Order order = orderService.createOrder(email);
        return ResponseEntity.ok(order);
    }

    // Lấy danh sách đơn hàng của user
    @GetMapping
    public ResponseEntity<List<Order>> getOrders(@RequestHeader("Authorization") String token) {
        String email = extractEmailFromToken(token);
        return ResponseEntity.ok(orderService.getOrdersByUser(email));
    }

    // Lấy chi tiết đơn hàng
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    // Hủy đơn hàng nếu chưa xử lý
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@RequestHeader("Authorization") String token, @PathVariable Long orderId) {
        String email = extractEmailFromToken(token);
        return ResponseEntity.ok(orderService.cancelOrder(email, orderId));
    }

    // Admin - Lấy danh sách tất cả đơn hàng
    @GetMapping("/admin/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // Admin - Cập nhật trạng thái đơn hàng
    @PutMapping("/admin/orders/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    // Phương thức giả lập để trích xuất email từ token
    private String extractEmailFromToken(String token) {
        return "test@gmail.com"; // Ở đây cần logic giải mã JWT
    }
}

