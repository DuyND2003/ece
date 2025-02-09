package com.example.ece.controller;

import com.example.ece.entity.Category;
import com.example.ece.entity.Product;
import com.example.ece.repository.ProductRepository;
import com.example.ece.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/id")
    public ResponseEntity<Product> getProductById(@PathVariable  Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Valid Product product){
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @PutMapping("/id")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody @Valid Product product){
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/id")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @GetMapping("/paged")
    public ResponseEntity<Map<String, Object>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size){

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", productPage.getContent());
        response.put("pageable", productPage.getPageable());
        response.put("totalPages", productPage.getTotalPages());
        response.put("totalElements", productPage.getTotalElements());
        response.put("size", productPage.getSize());
        response.put("number", productPage.getNumber());
        response.put("first", productPage.isFirst());
        response.put("last", productPage.isLast());
        response.put("numberOfElements", productPage.getNumberOfElements());
        response.put("empty", productPage.isEmpty());

        String baseUrl = "/products?page=";
        response.put("previous", productPage.isFirst() ? null : baseUrl + (page - 1) + "&size=" + size);
        response.put("next", productPage.isLast() ? null : baseUrl + (page + 1) + "&size=" + size);
        return ResponseEntity.ok((response));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable String category){
        Category categoryEnum = Category.valueOf(category.toUpperCase()); // String -> Enum
        List<Product> products = productRepository.findByCategory(categoryEnum);
        return ResponseEntity.ok(products);
    }
}
