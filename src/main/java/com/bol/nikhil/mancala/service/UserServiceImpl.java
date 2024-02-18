package com.bol.nikhil.mancala.service;

import com.bol.nikhil.mancala.model.User;
import com.bol.nikhil.mancala.model.UserStats;
import com.bol.nikhil.mancala.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    //constructor create
    @Autowired
    public UserServiceImpl( UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
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
