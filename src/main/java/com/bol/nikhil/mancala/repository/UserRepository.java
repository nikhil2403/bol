package com.bol.nikhil.mancala.repository;

import com.bol.nikhil.mancala.model.Game;
import com.bol.nikhil.mancala.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

}
