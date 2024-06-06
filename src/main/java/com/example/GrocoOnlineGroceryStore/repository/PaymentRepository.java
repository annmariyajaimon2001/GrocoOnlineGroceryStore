package com.example.GrocoOnlineGroceryStore.repository;


import com.example.GrocoOnlineGroceryStore.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
