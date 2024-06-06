package com.example.GrocoOnlineGroceryStore.repository;

import com.example.GrocoOnlineGroceryStore.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryName(String categoryName);
}
