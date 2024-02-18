package com.bol.nikhil.mancala.repository;

import com.bol.nikhil.mancala.model.Game;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor
@Data
@Builder
@Repository
public class GameRepository  {

    Map<Long,Game> gameRepository;

    //constructor
    @Autowired
    public GameRepository(Map<Long, Game> gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game save(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        if (game.getId() == null) {
            //set a random id
            game.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        }
        gameRepository.putIfAbsent(game.getId(),game);
        return game;
    }

    public <T> Optional<Game> findById(Long gameId) {
        return Optional.ofNullable(gameRepository.get(gameId));

    }
}
