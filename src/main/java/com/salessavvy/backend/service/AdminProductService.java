package com.salessavvy.backend.service;

import com.salessavvy.backend.entity.Category;
import com.salessavvy.backend.entity.Product;
import com.salessavvy.backend.entity.ProductImage;
import com.salessavvy.backend.repository.CategoryRepository;
import com.salessavvy.backend.repository.ProductImageRepository;
import com.salessavvy.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdminProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;

    public AdminProductService(ProductRepository productRepository, ProductImageRepository productImageRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product addProductWithImage(String name, String description, Double price, Integer stock, Integer categoryId, String imageUrl) {
        // Validate the category
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new IllegalArgumentException("Invalid category ID");
        }

        // Create and save the product
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(BigDecimal.valueOf(price));
        product.setStock(stock);
        product.setCategory(category.get());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);

        // Create and save the product image
        if (imageUrl != null && !imageUrl.isEmpty()) {
            ProductImage productImage = new ProductImage();
            productImage.setProduct(savedProduct);
            productImage.setImageUrl(imageUrl);
            productImageRepository.save(productImage);
        } else {
            throw new IllegalArgumentException("Product image URL cannot be empty");
        }

        return savedProduct;
    }

    public void deleteProduct(Integer productId) {
        // Check if the product exists
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("Product not found");
        }

        // Delete associated product images
        productImageRepository.deleteByProductId(productId);

        // Delete the product
        productRepository.deleteById(productId);
    }

    public Product modifyProduct(Integer productId, String name, String description, Double price, Integer stock, Category category, String imageUrl) {
        // Check if the product exists
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }

        // Update the product
        Product updatedProduct = product.get();
        updatedProduct.setName(name);
        updatedProduct.setDescription(description);
        updatedProduct.setPrice(BigDecimal.valueOf(price));
        updatedProduct.setStock(stock);
        updatedProduct.setCategory(category);

        // Update the product image
        Optional<ProductImage> productImage = productImageRepository.findByProductProductId(productId);

        if (productImage.isPresent()) {
            productImage.get().setImageUrl(imageUrl);
            productImageRepository.save(productImage.get());
        } else {
            throw new IllegalArgumentException("Product image not found");
        }

        // Save the updated product
        return productRepository.save(updatedProduct);
    }
}
