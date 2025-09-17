package com.salessavvy.backend.controller;

import com.salessavvy.backend.entity.User;
import com.salessavvy.backend.repository.UserRepository;
import com.salessavvy.backend.service.CartService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/cart")
public class CartController {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CartController.class);
    private final UserRepository userRepository;
    private final CartService cartService;

    public CartController(UserRepository userRepository, CartService cartService) {
        this.userRepository = userRepository;
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(@RequestBody Map<String, Object> request) {

        String username = (String) request.get("username");
        int productId = (int) request.get("productId");
        int quantity = request.containsKey("quantity") ? ((Number) request.get("quantity")).intValue() : 1;

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        cartService.addToCart(user.getId(), productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/items-count")
    public ResponseEntity<?> cartItemsCount(HttpServletRequest request) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        int count = cartService.cartItemsCount(authenticatedUser.getId());

        return ResponseEntity.ok(count);
    }
}
