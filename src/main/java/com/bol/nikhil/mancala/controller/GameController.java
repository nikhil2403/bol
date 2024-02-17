package com.bol.nikhil.mancala.controller;

import com.bol.nikhil.mancala.dto.CreateGameResponse;
import com.bol.nikhil.mancala.dto.GameMoveResponse;
import com.bol.nikhil.mancala.model.Game;
import com.bol.nikhil.mancala.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;


@RestController
@RequestMapping("/games")
@Slf4j
public class GameController {

    private GameService gameService;

    @Autowired
    public GameController(GameService gameService){
        this.gameService = gameService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateGameResponse createGame(){
        log.info("Entering create game");
        Game game = gameService.createGame();
        return CreateGameResponse.builder()
                .id(String.valueOf(game.getId()))
                .build();
    }


    @PutMapping("/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public Game startGame(@PathVariable final Long gameId){
        log.info("Entering startGame with gameId : %s",gameId);
        return gameService.startGame(gameId);
    }



    @GetMapping("/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public Game getGame(@PathVariable final Long gameId){
        log.info("Entering getGameStatus with gameId : %s",gameId);
        return gameService.getGame(gameId);
    }


    //register user with game
    @PostMapping("/{gameId}/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Game registerUser(@PathVariable final Long gameId, @PathVariable final Long userId){
        log.info("Entering registerUser with gameId : %s and userId : %s",gameId,userId);
        return gameService.registerUserWithGame(gameId, userId);
    }


    @PutMapping("/{gameId}/pits/{pitId}")
    @ResponseStatus(HttpStatus.OK)
    public GameMoveResponse makeMove(@PathVariable final Long gameId, @PathVariable final Integer pitId, @RequestHeader("userId") Long userId){
        log.info("Entering makeMove with gameId : %s and userId : %s and pitId : %s",gameId,userId,pitId);
      Game game =   gameService.makeMove(gameId, pitId,userId);
        return GameMoveResponse.builder()
                .game(game)
                .status(new LinkedHashMap<>())
                .build();
    }
}
