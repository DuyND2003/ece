package com.example.ece.service;

import com.example.ece.entity.*;
import com.example.ece.repository.OrderItemRepository;
import com.example.ece.repository.OrderRepository;
import com.example.ece.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final UserRepository userRepository;

    public Order createOrder(String email) {
        Cart cart = cartService.getCartByUser(email);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot create order.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getProduct().getPrice())
                        .build())
                .collect(Collectors.toList());

        Order order = Order.builder()
                .user(user)
                .orderItems(orderItems)
                .totalPrice(orderItems.stream()
                        .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(order);
        orderItems.forEach(orderItem -> {
            orderItem.setOrder(order);
            orderItemRepository.save(orderItem);
        });

        cartService.clearCart(email);

        return order;
    }

    public List<Order> getOrdersByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUser(user);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order cancelOrder(String email, Long orderId) {
        Order order = getOrderById(orderId);
        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You cannot cancel this order.");
        }
        order.setStatus(OrderStatus.CANCELED);
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
