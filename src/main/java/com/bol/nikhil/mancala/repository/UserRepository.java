package com.bol.nikhil.mancala.repository;

import com.bol.nikhil.mancala.model.Game;
import com.bol.nikhil.mancala.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserRepository  {
    Map<Long,User> userRepository;

    /**
     * Save user to repository and return the saved user .
     * If user is null throw IllegalArgumentException
     * If user id is null set a random id
     *  @param user
     * @return  User
     *
     */
    public User save(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getId() == null) {
            //set a random id
            user.setId((long) (Math.random() * 1000));
        }
        userRepository.putIfAbsent(user.getId(),user);
        return user;

    }

    /**
     * Find user by id and return the user if found else return null
     *  @param userId
     *  @return  Optional<User>
     */
    public Optional<User> findById(Long userId) {

        return Optional.ofNullable(userRepository.get(userId));

    }
}
