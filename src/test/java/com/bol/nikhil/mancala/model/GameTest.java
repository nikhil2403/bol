package com.bol.nikhil.mancala.model;

import com.bol.nikhil.mancala.exception.GameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//create test cases for the game class

class GameTest {


    @Test
    void initialize() {
        //test the initialize method
        Game game = new Game();
        game.initialize();
        //check if the game is initialized
        assertEquals(GameStatus.INITIALIZED, game.getGameStatus());
        //check if the player is active
        assertNull(game.getPlayerActive());
        //check if the house pits are initialized
        assertEquals(6, game.getHousePits().get(0));
        assertEquals(13, game.getHousePits().get(1));
    }

    @Test
    void registerUser() {
        //test the register user method
        Game game = new Game();
        game.initialize();
        //register a user
        game.registerUser(Player.builder().userId(1L).build());
        //check if the player is active
        assertEquals(Player.builder().userId(1L).build(), game.getPlayerActive());
       //register another user
        game.registerUser(Player.builder().userId(2L).build());
        //check if another player is waiting
        assertEquals(Player.builder().userId(2L).build(), game.getWaitingPlayer());

        //throw exception if the game is already started
        assertThrows(GameException.class, () -> game.registerUser(Player.builder().userId(3L).build()));



    }

    @Test
    void startGame() {
        //test the start game method
        Game game = new Game();
        game.initialize();
        //register a user
        game.registerUser(Player.builder().userId(1L).build());
        //register another user
        game.registerUser(Player.builder().userId(2L).build());
        //start the game
        game.startGame();
        //check if the game is started
        assertEquals(GameStatus.IN_PROGRESS, game.getGameStatus());

    }

    @Test
    void makeMove() {
        //test the make move method
        Game game = new Game();
        game.initialize();
        //register a user
        game.registerUser(Player.builder().userId(1L).build());
        //register another user
        game.registerUser(Player.builder().userId(2L).build());
        //start the game
        game.startGame();
        //make a move
        game.makeMove(0);
        //check if active player pits are updated and the next player is active
        assertEquals(0, game.getPits().get(0).getStoneCount());
        assertEquals(7, game.getPits().get(1).getStoneCount());
        assertEquals(7, game.getPits().get(2).getStoneCount());
        assertEquals(7, game.getPits().get(3).getStoneCount());
        assertEquals(7, game.getPits().get(4).getStoneCount());
        assertEquals(7, game.getPits().get(5).getStoneCount());
        assertEquals(1, game.getPits().get(6).getStoneCount());
        //other player pits not updated
        assertEquals(6, game.getPits().get(7).getStoneCount());
        assertEquals(6, game.getPits().get(8).getStoneCount());
        assertEquals(6, game.getPits().get(9).getStoneCount());
        assertEquals(6, game.getPits().get(10).getStoneCount());
        assertEquals(6, game.getPits().get(11).getStoneCount());
        assertEquals(6, game.getPits().get(12).getStoneCount());
        assertEquals(0, game.getPits().get(13).getStoneCount());


        assertEquals(1L, game.getPlayerActive().getUserId());
        //make a move
    }

    @Test
    void testOtherPlayersStonesTaken() {
        //test the make move method
        Game game = new Game();
        game.initialize();
        game.registerUser(Player.builder().userId(1L).build());
        //register another user
        game.registerUser(Player.builder().userId(2L).build());
        game.startGame();

        // Set up a specific game state where player 1 can capture player 2's stones
       game.getPits().get(0).setStoneCount(1);
       game.getPits().get(1).setStoneCount(0);
       game.getPits().get(6).setStoneCount(0);
       game.getPits().get(10).setStoneCount(6);

        // Player 1 makes a move that results in capturing player 2's stones
        game.makeMove(0);

        // Check if player 1's score increased by the number of captured stones
        assertEquals(7, game.getPlayerOne().getScore());
        // Check if player 2's stones were captured
        assertEquals(0, game.getPits().get(11).getStoneCount());

    }

    @Test
    void testMoveFromHousePit() {
        //test the make move method
        Game game = new Game();
        game.initialize();
        game.registerUser(Player.builder().userId(1L).build());
        //register another user
        game.registerUser(Player.builder().userId(2L).build());
        game.startGame();

        // Set up a specific game state where player 1 can capture player 2's stones
       game.getPits().get(0).setStoneCount(0);
       game.getPits().get(1).setStoneCount(0);
       game.getPits().get(6).setStoneCount(0);
       game.getPits().get(10).setStoneCount(6);



        // Player 1 makes a move that is from house pit should throw exception
        assertThrows(GameException.class, () -> game.makeMove(6));
        game.makeMove(3);
        // Player 2 makes a move that is from house pit should throw exception
        assertThrows(GameException.class, () -> game.makeMove(13));

    }

    @Test
    void testGameWon() {
        //test the make move method
        Game game = new Game();
        game.initialize();
        game.registerUser(Player.builder().userId(1L).build());
        //register another user
        game.registerUser(Player.builder().userId(2L).build());
        game.startGame();

        // Set up a specific game state where player 1 can capture player 2's stones
       game.getPits().get(0).setStoneCount(0);
       game.getPits().get(1).setStoneCount(0);
       game.getPits().get(2).setStoneCount(0);
       game.getPits().get(3).setStoneCount(0);
       game.getPits().get(4).setStoneCount(1);
       game.getPits().get(5).setStoneCount(0);
       game.getPits().get(6).setStoneCount(0);
       game.getPits().get(7).setStoneCount(5);
       game.getPits().get(8).setStoneCount(7);
       game.getPits().get(9).setStoneCount(4);
       game.getPits().get(10).setStoneCount(0);
       game.getPits().get(11).setStoneCount(4);
       game.getPits().get(12).setStoneCount(3);
       game.getPits().get(13).setStoneCount(0);

        // Player 1 makes a move that is from house pit should throw exception
        game.makeMove(4);

        //check if the game is won
        assertEquals(GameStatus.FINISHED, game.getGameStatus());



    }
}