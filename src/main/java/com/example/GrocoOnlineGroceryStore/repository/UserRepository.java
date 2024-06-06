package com.example.GrocoOnlineGroceryStore.repository;


import com.example.GrocoOnlineGroceryStore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmailAndPassword(String email, String password);
    User findByEmail(String email);
}
