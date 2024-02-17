package com.bol.nikhil.mancala.repository;

import com.bol.nikhil.mancala.model.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game,Long> {

}
