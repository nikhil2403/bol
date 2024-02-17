package com.bol.nikhil.mancala.service;

import com.bol.nikhil.mancala.model.User;
import com.bol.nikhil.mancala.model.UserStats;

import java.util.Optional;

public interface UserService {
    User createUser(Long userId);
    Optional<User> getUser(Long userId);

   UserStats getUserStats(Long userId);

}
