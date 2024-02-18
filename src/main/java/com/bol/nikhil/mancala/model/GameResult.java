package com.bol.nikhil.mancala.model;

import com.bol.nikhil.mancala.exception.GameException;
import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameResult {
    @Getter(AccessLevel.NONE)
    private Game game;
    private int winningScore;
    private int losingScore;
    private Long winningPlayerId;
    private Long losingPlayerId;
    private GameStatus gameStatus;

   public  GameResult(Game game){
        this.game = game;
        this.gameStatus = game.getGameStatus();
        this.winningScore = getWinningScore();
        this.losingScore = getLosingScore();
        this.winningPlayerId = getWinningPlayerId();
        this.losingPlayerId = getLosingPlayerId();
    }

    public int getWinningScore() {
        if(gameStatus.equals(GameStatus.FINISHED) || gameStatus.equals(GameStatus.DRAW)){
            int playerOneScore = this.game.getPits().get(this.game.getPlayerOne().getPlayerTurn().getHousePit()).getStoneCount();
            int playerTwoScore = this.game.getPits().get(this.game.getPlayerTwo().getPlayerTurn().getHousePit()).getStoneCount();
            return Math.max(playerOneScore, playerTwoScore);
        }
        throw new
                GameException(HttpStatus.BAD_REQUEST.value() ,"Game is not finished yet");
    }

    public int getLosingScore() {
        if(gameStatus.equals(GameStatus.FINISHED) || gameStatus.equals(GameStatus.DRAW)){
            int playerOneScore = this.game.getPits().get(this.game.getPlayerOne().getPlayerTurn().getHousePit()).getStoneCount();
            int playerTwoScore = this.game.getPits().get(this.game.getPlayerTwo().getPlayerTurn().getHousePit()).getStoneCount();
            return Math.min(playerOneScore, playerTwoScore);
        }
        throw new
                GameException(HttpStatus.BAD_REQUEST.value() ,"Game is not finished yet");
    }
    public Long getWinningPlayerId() {
        if(gameStatus.equals(GameStatus.FINISHED) || gameStatus.equals(GameStatus.DRAW)){
            int playerOneScore = this.game.getPits().get(this.game.getPlayerOne().getPlayerTurn().getHousePit()).getStoneCount();
            int playerTwoScore = this.game.getPits().get(this.game.getPlayerTwo().getPlayerTurn().getHousePit()).getStoneCount();
            return playerOneScore > playerTwoScore ? this.game.getPlayerOne().getUserId() : this.game.getPlayerTwo().getUserId();
        }
        throw new
                GameException(HttpStatus.BAD_REQUEST.value() ,"Game is not finished yet");
    }
    public Long getLosingPlayerId() {
        if(gameStatus.equals(GameStatus.FINISHED) || gameStatus.equals(GameStatus.DRAW)){
            int playerOneScore = this.game.getPits().get(this.game.getPlayerOne().getPlayerTurn().getHousePit()).getStoneCount();
            int playerTwoScore = this.game.getPits().get(this.game.getPlayerTwo().getPlayerTurn().getHousePit()).getStoneCount();
            return playerOneScore < playerTwoScore ? this.game.getPlayerOne().getUserId() : this.game.getPlayerTwo().getUserId();
        }
        throw new
                GameException(HttpStatus.BAD_REQUEST.value() ,"Game is not finished yet");
    }

}
