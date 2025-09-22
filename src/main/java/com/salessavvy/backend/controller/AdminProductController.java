package com.salessavvy.backend.controller;

import com.salessavvy.backend.entity.Category;
import com.salessavvy.backend.entity.Product;
import com.salessavvy.backend.service.AdminProductService;
import com.salessavvy.backend.service.CategoryService;
import com.salessavvy.backend.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/admin/products")
public class AdminProductController {

    private final AdminProductService adminProductService;
    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminProductController(AdminProductService adminProductService, ProductService productService, CategoryService categoryService) {
        this.adminProductService = adminProductService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            products.sort(Comparator.comparing(Product::getCreatedAt).reversed());
            List<Map<String, Object>> productList = new ArrayList<>();
            for (Product product : products) {
                Map<String, Object> productDetails = new HashMap<>();
                productDetails.put("product_id", product.getProductId());
                productDetails.put("name", product.getName());
                productDetails.put("description", product.getDescription());
                productDetails.put("price", product.getPrice());
                productDetails.put("stock", product.getStock());
                List<String> images = productService.getProductImages(product.getProductId());
                productDetails.put("images", images);
                productDetails.put("category", product.getCategory().getCategoryName());
                productList.add(productDetails);
            }
            return ResponseEntity.ok(productList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.status(HttpStatus.OK).body(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody Map<String, Object> productRequest) {
        try {
            String name = (String) productRequest.get("name");
            String description = (String) productRequest.get("description");
            Double price = Double.valueOf(String.valueOf(productRequest.get("price")));
            Integer stock = (Integer) productRequest.get("stock");
            String imageUrl = (String) productRequest.get("imageUrl");
            String cat = (String) productRequest.get("category");
            Category category = categoryService.getCategoryByName(cat);
            Product addedProduct = adminProductService.addProductWithImage(name, description, price, stock, category.getCategoryId(), imageUrl);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> modifyProduct(@RequestBody Map<String, Object> userRequest, @PathVariable Integer productId) {
        try {
            String name = (String) userRequest.get("name");
            String description = (String) userRequest.get("description");
            String cat = (String) userRequest.get("category");
            String imageUrl = (String) userRequest.get("image");
            Double price = Double.valueOf(String.valueOf(userRequest.get("price")));
            Integer stock = (Integer) userRequest.get("stock");
            Category category = categoryService.getCategoryByName(cat);
            Product updatedProduct = adminProductService.modifyProduct(productId, name, description, price, stock, category, imageUrl);
            return ResponseEntity.status(HttpStatus.OK).body("Updated product successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        try {
            adminProductService.deleteProduct(productId);
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }
}
