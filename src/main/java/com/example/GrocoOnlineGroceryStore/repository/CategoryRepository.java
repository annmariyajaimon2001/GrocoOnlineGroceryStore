package com.example.GrocoOnlineGroceryStore.repository;


import com.example.GrocoOnlineGroceryStore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
