package com.example.GrocoOnlineGroceryStore.service;

import com.example.GrocoOnlineGroceryStore.entity.User;
import com.example.GrocoOnlineGroceryStore.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {

        return userRepository.save(user);
    }
    public User login(String email, String password) {

        return  userRepository.findByEmailAndPassword(email, password);

    }
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }



}
