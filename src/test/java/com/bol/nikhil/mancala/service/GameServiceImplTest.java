package com.bol.nikhil.mancala.service;

import com.bol.nikhil.mancala.exception.GameException;
import com.bol.nikhil.mancala.model.GameResult;
import com.bol.nikhil.mancala.model.GameStatus;
import com.bol.nikhil.mancala.model.User;
import com.bol.nikhil.mancala.repository.GameRepository;
import com.bol.nikhil.mancala.repository.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.bol.nikhil.mancala.model.Game;

import java.util.HashMap;

class GameServiceImplTest {

    @Test
    void startGame() {
        //test the start game method
        GameRepository gameRepository = new GameRepository(new HashMap<>());
        UserRepository userRepository = new UserRepository(new HashMap<>());
        UserService userService = new UserServiceImpl(userRepository);

        User user1 = userService.createUser(User.builder().build());
        User user2 = userService.createUser(User.builder().build());

        GameServiceImpl gameService = new GameServiceImpl(gameRepository, userService );
        //create a game
        Game game = gameService.createGame();



        //register a user
        gameService.registerUserWithGame(game.getId(), user1.getId());
        //register another user
        gameService.registerUserWithGame(game.getId(), user2.getId());

        //start the game
        gameService.startGame(game.getId());
        //check if the game is started
        assertEquals(GameStatus.IN_PROGRESS, game.getGameStatus());

    }

    @Test
    void createGame() {
        //test the create game method
        GameRepository gameRepository = new GameRepository(new HashMap<>());
        UserRepository userRepository = new UserRepository(new HashMap<>());
        UserService userService = new UserServiceImpl(userRepository);
        //create a game service
        GameServiceImpl gameService = new GameServiceImpl(gameRepository, userService );
        //create a game
        Game game = gameService.createGame();
        //check if the game is created
        assertNotNull(game);
        //check game status is initialized
        assertEquals(GameStatus.INITIALIZED, game.getGameStatus());

    }

    @Test
    void getGame() {
        //test the get game method
        GameRepository gameRepository = new GameRepository(new HashMap<>());
        UserRepository userRepository = new UserRepository(new HashMap<>());
        UserService userService = new UserServiceImpl(userRepository);
        //create a game service
        GameServiceImpl gameService = new GameServiceImpl(gameRepository, userService );
        //create a game
        Game game = gameService.createGame();
        //get the game
        Game game1 = gameService.getGame(game.getId());
        //check if the game is created
        assertNotNull(game1);
        //check game status is initialized
        assertEquals(GameStatus.INITIALIZED, game1.getGameStatus());
    }

    @Test
    void getGameStats() {
        //test the get game stats method
        GameRepository gameRepository = new GameRepository(new HashMap<>());
        UserRepository userRepository = new UserRepository(new HashMap<>());
        UserService userService = new UserServiceImpl(userRepository);

        User user1 = userService.createUser(User.builder().build());
        User user2 = userService.createUser(User.builder().build());

        GameServiceImpl gameService = new GameServiceImpl(gameRepository, userService );
        //create a game
        Game game = gameService.createGame();




        //register a user
        gameService.registerUserWithGame(game.getId(), user1.getId());
        //register another user
        gameService.registerUserWithGame(game.getId(), user2.getId());

        //start the game
        gameService.startGame(game.getId());
        //get the game stats should throw exception because game is not finished yet
        assertThrows(GameException.class, () -> gameService.getGameStats(game.getId()));

        //set player one as winner and finish the game. set score for player one and player two
        game.getPits().get(game.getHousePits().get(0)).setStoneCount(25);
        game.getPits().get(game.getHousePits().get(1)).setStoneCount(20);

        game.setGameStatus(GameStatus.FINISHED);
        //get the game stats
        GameResult gameResult = gameService.getGameStats(game.getId());
        //check if the game result is created
        assertNotNull(gameResult);
        //check if the game result is finished
        assertEquals(GameStatus.FINISHED, gameResult.getGameStatus());
        //check if the game result has a winner
        assertEquals(game.getPlayerOne().getUserId(), gameResult.getWinningPlayerId());


    }

    @Test
    void makeMove() {
    }

    @Test
    void registerUserWithGame() {
    }
}