package com.salessavvy.backend.service;

import com.salessavvy.backend.entity.CartItem;
import com.salessavvy.backend.entity.Product;
import com.salessavvy.backend.entity.User;
import com.salessavvy.backend.repository.CartRepository;
import com.salessavvy.backend.repository.ProductRepository;
import com.salessavvy.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    UserRepository userRepository;
    ProductRepository productRepository;
    CartRepository cartRepository;

    public CartService(UserRepository userRepository, ProductRepository productRepository,
            CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    public void addToCart(int userId, int productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        Optional<CartItem> existingItem = cartRepository.findByUserAndProduct(userId, productId);

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartRepository.save(cartItem);
        } else {
            CartItem newItem = new CartItem(user, product, quantity);
            cartRepository.save(newItem);
        }
    }

    public int cartItemsCount(int userId) {
        return cartRepository.countTotalItems(userId);
    }
}
