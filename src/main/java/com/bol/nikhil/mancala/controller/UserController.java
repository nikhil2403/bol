package com.bol.nikhil.mancala.controller;

import com.bol.nikhil.mancala.model.User;
import com.bol.nikhil.mancala.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

/**
 * User Controller
 * This class is responsible for handling all the user related requests
 * It has the following endpoints
 * 1. createUser - Create a new user
 * 2. getUser - Get the user details
 */
@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

   private UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * Create a new user. This will create a new user and return the user details with the user id created
     * @param user
     * @return
     */
    @PostMapping("/createUser")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user){
        log.info("Entering create user");
        return userService.createUser(user);
    }

    /**
     *  Get the user details. This will return the user details with the user id provided.
     *  If the user is not found, it will throw an exception
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable final Long userId){
        log.info("Entering get user with userId : %s",userId);
        return userService.getUser(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

}
