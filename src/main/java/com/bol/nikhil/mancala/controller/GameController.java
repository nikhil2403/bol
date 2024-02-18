package com.bol.nikhil.mancala.controller;


import com.bol.nikhil.mancala.model.Game;
import com.bol.nikhil.mancala.model.GameResult;
import com.bol.nikhil.mancala.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;


/**
 * Game Controller
 * This class is responsible for handling all the game related requests
 * It has the following endpoints
 * 1. createGame - Create a new game
 * 2. startGame - Start the game
 * 3. getGame - Get the game status
 * 4. registerUser - Register user with game
 * 5. makeMove - Make a move in the game
 * 6. getGameStats - Get the game stats
 *
 */
@RestController
@RequestMapping("/games")
@Slf4j
public class GameController {

    private GameService gameService;

    @Autowired
    public GameController(GameService gameService){
        this.gameService = gameService;
    }

    /**
     * Create a new game
     * @return Game object with the game details and status as INITIALIZED
     */
    @PostMapping("/createGame")
    @ResponseStatus(HttpStatus.CREATED)
    public Game createGame(){
        log.info("Entering create game");
        Game game = gameService.createGame();
        return game;

    }

    /**
     * Start the game. This will change the game status to IN_PROGRESS if both players are registered ,otherwise it will throw an exception.
     * @param gameId
     * @return
     */
    @PutMapping("/startGame/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public Game startGame(@PathVariable final Long gameId){
        log.info("Entering startGame with gameId : %s",gameId);
        return gameService.startGame(gameId);
    }


    /**
     * Get the game status. This will return the game details and the status of the game.
     * @param gameId
     * @return
     */
    @GetMapping("/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public Game getGame(@PathVariable final Long gameId){
        log.info("Entering getGameStatus with gameId : %s",gameId);
        return gameService.getGame(gameId);
    }


    /**
     * Register user with game. This will register the user with the game and return the game details. If both players are registered, this api will throw an exception.
     * If no user is registered, the first user will be registered as active player and the second user will be registered as waiting player.
     * If one user is already registered, the second user will be registered as waiting player.
     * @param gameId
     * @param userId
     * @return
     */
    @PostMapping("/register/{gameId}/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Game registerUser(@PathVariable final Long gameId, @PathVariable final Long userId){
        log.info("Entering registerUser with gameId : %s and userId : %s",gameId,userId);
        return gameService.registerUserWithGame(gameId, userId);
    }

    /**
     * Make a move in the game. This will make a move in the game and return the game details provided move is valid, otherwise it will throw an exception.
     * Details of the move will be updated in the game object. Rules of vaiid move are as follows
     * 1. The pit should not be empty
     * 2. The pit should not be house pit
     * 3. The pit should be active player's pit with at least 1 stone
     * 4. The pit should not be opponent's house pit
     * 5. The pit should not be opponent's empty pit
     * 6. The pit should not be active player's house pit
     * 7. The pit should not be active player's empty pit
     * 8. The pit should not be opponent's pit
     * @param gameId
     * @param pitId
     * @param userId
     * @return
     */

    @PutMapping("/playmove/{gameId}/pits/{pitId}/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Game makeMove(@PathVariable final Long gameId, @PathVariable final Integer pitId, @PathVariable Long userId){
        log.info("Entering makeMove with gameId : %s and userId : %s and pitId : %s",gameId,userId,pitId);
      return gameService.makeMove(gameId, pitId,userId);
    }

    /**
     * Get the game stats such as winner, game status, scores etc. for the game with gameId.
     * This will return the game result with the game details and the status of the game.
     * If the game is not finished, it will throw an exception.
     *
     * @param gameId
     * @return
     */
    @GetMapping("/gamestats/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public GameResult getGameStats(@PathVariable final Long gameId){
        log.info("Entering getGameStats with gameId : %s",gameId);
       return gameService.getGameStats(gameId);
    }
}
