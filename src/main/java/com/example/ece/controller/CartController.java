package com.example.ece.controller;

import com.example.ece.entity.Cart;
import com.example.ece.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(cartService.getCartByUser(userDetails.getUsername()));
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestParam Long productId, @RequestParam int quantity){
        return ResponseEntity.ok(cartService.addToCart(userDetails.getUsername(), productId, quantity));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Cart> removeFromCart(@AuthenticationPrincipal UserDetails userDetails
                                               , @RequestParam Long productId){
        return ResponseEntity.ok(cartService.removeFromCart(userDetails.getUsername(), productId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetails userDetails){
        cartService.clearCart(userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
