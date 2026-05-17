package com.coffeeshop.repository;

import com.coffeeshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByAvailableTrue();

    List<Product> findByFeaturedTrueAndAvailableTrue();

    List<Product> findByCategoryAndAvailableTrue(String category);

    List<Product> findByNameContainingIgnoreCaseAndAvailableTrue(String keyword);

    List<Product> findByCategory(String category);
}
