package com.example.ece.service;

import com.example.ece.entity.Product;
import com.example.ece.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    @Cacheable(value = "products", key = "#page")
    public Page<Product> getProducts(int page, int size) {
        System.out.println("Fetching products from DB...");
        return productRepository.findAll(PageRequest.of(page, size));
    }

    @Cacheable(value = "allProducts")
    public List<Product> getAllProducts() {
        System.out.println("Fetching all products from DB...");
        return productRepository.findAll();
    }

    @Cacheable(value = "product", key = "#id")
    public Product getProductById(Long id) {
        System.out.println("Fetching product from DB...");
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @CacheEvict(value = {"products", "allProducts"}, allEntries = true)
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @CacheEvict(value = {"products", "allProducts", "product"}, allEntries = true)
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStockQuantity(product.getStockQuantity());
        existingProduct.setImageUrl(product.getImageUrl());
        return productRepository.save(existingProduct);
    }

    @CacheEvict(value = {"products", "allProducts", "product"}, allEntries = true)
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
