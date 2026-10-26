package com.rikkei.product.controller;

import com.rikkei.product.dto.ProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private static final List<ProductDTO> PRODUCTS = new ArrayList<>(List.of(
            new ProductDTO(1L, "Laptop Pro", 1299.99),
            new ProductDTO(2L, "Wireless Mouse", 29.99),
            new ProductDTO(3L, "Mechanical Keyboard", 149.99)
    ));

    @GetMapping
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(PRODUCTS);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRODUCT_CREATE')")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO product) {
        ProductDTO newProduct = new ProductDTO((long) (PRODUCTS.size() + 1), product.name(), product.price());
        PRODUCTS.add(newProduct);
        return ResponseEntity.ok(newProduct);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_CREATE')")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        boolean removed = PRODUCTS.removeIf(p -> p.id().equals(id));
        if (removed) {
            return ResponseEntity.ok(Map.of("message", "Product " + id + " deleted successfully"));
        }
        return ResponseEntity.notFound().build();
    }
}
