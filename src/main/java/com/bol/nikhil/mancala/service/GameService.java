package com.bol.nikhil.mancala.service;

import com.bol.nikhil.mancala.model.Game;
import com.bol.nikhil.mancala.model.GameResult;

public interface GameService {
    public Game startGame(Long gameId);
    public Game createGame();

    Game getGame(Long gameId);

    public GameResult getGameStats(Long gameId);




    public Game makeMove(Long gameId, int pitId, Long userId);

    Game registerUserWithGame(Long gameId, Long userId);
}
