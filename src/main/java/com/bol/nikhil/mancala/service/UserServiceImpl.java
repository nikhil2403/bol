package com.bol.nikhil.mancala.service;

import com.bol.nikhil.mancala.model.User;
import com.bol.nikhil.mancala.model.UserStats;
import com.bol.nikhil.mancala.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Override
    public User createUser(Long userId) {
        return userRepository.save(User.builder().id(userId).build());
    }

    @Override
    public Optional<User> getUser(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public UserStats getUserStats(Long userId) {
        return null;
    }
}
