package com.example.ece.service;

import com.example.ece.entity.Cart;
import com.example.ece.entity.CartItem;
import com.example.ece.entity.Product;
import com.example.ece.entity.User;
import com.example.ece.repository.CartItemRepository;
import com.example.ece.repository.CartRepository;
import com.example.ece.repository.ProductRepository;
import com.example.ece.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    // Lay gio hang cua nguoi dung
    public Cart getCartByUser(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        // Tìm giỏ hàng của user, nếu chưa có thì tạo mới giỏ hàng rỗng
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).items(new ArrayList<>()).build()));
    }

    public Cart addToCart(String email, Long productId, int quantity){
        Cart cart = getCartByUser(email);

        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if(existingItem.isPresent()){
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else{
            // Neu chua co, tao moi CartItem va them vao gio hang
            CartItem newItem = CartItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .build();
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    public Cart removeFromCart(String email, Long productId){
        Cart cart = getCartByUser(email);

        // Xoa san pham co Id tuong ung khoi danh sach san pham trong gio hang
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        return cartRepository.save(cart);
    }

    public void clearCart(String email){
        Cart cart = getCartByUser(email);
        cart.getItems().clear();
        cartRepository.save(cart);
    }


}
