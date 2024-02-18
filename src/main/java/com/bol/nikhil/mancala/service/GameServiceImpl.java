package com.bol.nikhil.mancala.service;

import com.bol.nikhil.mancala.exception.GameException;
import com.bol.nikhil.mancala.model.Game;
import com.bol.nikhil.mancala.model.GameResult;
import com.bol.nikhil.mancala.model.Player;
import com.bol.nikhil.mancala.model.User;
import com.bol.nikhil.mancala.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.List;

import static com.bol.nikhil.mancala.util.Constants.*;

@Service
public class GameServiceImpl implements GameService {

    GameRepository gameRepository;

    UserService userService;

    //constructor create
    @Autowired
    public GameServiceImpl(GameRepository gameRepository, UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
    }


    @Override
    public Game startGame(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameException(HttpStatus.INTERNAL_SERVER_ERROR.value(), GAME_NOT_FOUND));
        game.startGame();
        return gameRepository.save(game);
    }

    @Override
    public Game createGame() {
      Game game =  Game.builder().build();
      game.initialize();
      return gameRepository.save(game);
    }

    @Override
    public Game getGame(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new GameException(HttpStatus.INTERNAL_SERVER_ERROR.value(), GAME_NOT_FOUND));
    }

    @Override
    public GameResult getGameStats(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameException(HttpStatus.INTERNAL_SERVER_ERROR.value(), GAME_NOT_FOUND));
        return new GameResult(game);

    }


    @Override
    public Game makeMove(Long gameId, int pitId, Long userId) {
        User user = userService.getUser(userId).orElseThrow(() -> new GameException(HttpStatus.INTERNAL_SERVER_ERROR.value(), USER_NOT_FOUND));
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameException(HttpStatus.INTERNAL_SERVER_ERROR.value(), GAME_NOT_FOUND));
        if(!game.isPlayerTurn(user)){
            throw new GameException(HttpStatus.INTERNAL_SERVER_ERROR.value(), NOT_ACTIVE_PLAYER_TURN);
        }
        game.makeMove(pitId);

      return  gameRepository.save(game);

    }

    @Override
    public Game registerUserWithGame(Long gameId, Long userId) {
        User user = userService.getUser(userId).orElseThrow(() -> new GameException(HttpStatus.INTERNAL_SERVER_ERROR.value(),USER_NOT_FOUND));
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameException(HttpStatus.INTERNAL_SERVER_ERROR.value(), GAME_NOT_FOUND ));
        Player player = Player.builder().userId(user.getId()).build();
        game.registerUser(player);
        return gameRepository.save(game);
    }

    @Override
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }


}
