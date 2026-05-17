package com.coffeeshop.service;

import com.coffeeshop.entity.Product;
import com.coffeeshop.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllAvailable() {
        return productRepository.findByAvailableTrue();
    }

    public List<Product> getFeatured() {
        return productRepository.findByFeaturedTrueAndAvailableTrue();
    }

    public List<Product> getByCategory(String category) {
        return productRepository.findByCategoryAndAvailableTrue(category);
    }

    public List<Product> search(String keyword) {
        return productRepository.findByNameContainingIgnoreCaseAndAvailableTrue(keyword);
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
    }

    // ── Admin ────────────────────────────────────────────

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public Product toggleAvailability(Long id) {
        Product product = getById(id);
        product.setAvailable(!product.isAvailable());
        return productRepository.save(product);
    }
}
