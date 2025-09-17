package com.salessavvy.backend.service;

import com.salessavvy.backend.entity.CartItem;
import com.salessavvy.backend.entity.Product;
import com.salessavvy.backend.entity.ProductImage;
import com.salessavvy.backend.entity.User;
import com.salessavvy.backend.repository.CartRepository;
import com.salessavvy.backend.repository.ProductImageRepository;
import com.salessavvy.backend.repository.ProductRepository;
import com.salessavvy.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {
    private final ProductImageRepository productImageRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    CartRepository cartRepository;

    public CartService(UserRepository userRepository, ProductRepository productRepository,
                       CartRepository cartRepository, ProductImageRepository productImageRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.productImageRepository = productImageRepository;
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

    public Map<String, Object> getCartItems(int userId) {
        List<CartItem> cartItems = cartRepository.findCartItemsWithProductDetails(userId);

        Map<String, Object> response = new HashMap<>();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        response.put("username", user.getUsername());
        response.put("role", user.getRole().toString());

        List<Map<String, Object>> products = new ArrayList<>();
        double overallTotalPrice = 0;

        for (CartItem cartItem : cartItems) {
            Map<String, Object> productDetails = new HashMap<>();

            Product product = cartItem.getProduct();

            List<ProductImage> productImages = productImageRepository.findByProduct_ProductId(product.getProductId());
            String imageUrl = (productImages != null && !productImages.isEmpty())
                    ? productImages.get(0).getImageUrl()
                    : "default-image-url";

            productDetails.put("product_id", product.getProductId());
            productDetails.put("image_url", imageUrl);
            productDetails.put("name", product.getName());
            productDetails.put("description", product.getDescription());
            productDetails.put("price_per_unit", product.getPrice());
            productDetails.put("quantity", cartItem.getQuantity());
            productDetails.put("total_price", cartItem.getQuantity() * product.getPrice().doubleValue());

            products.add(productDetails);

            overallTotalPrice += cartItem.getQuantity() * product.getPrice().doubleValue();
        }

        Map<String, Object> cart = new HashMap<>();
        cart.put("products", products);
        cart.put("overall_total_price", overallTotalPrice);

        response.put("cart", cart);

        return response;
    }
}
