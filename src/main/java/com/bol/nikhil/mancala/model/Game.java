package com.bol.nikhil.mancala.model;

import com.bol.nikhil.mancala.exception.GameException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.*;

import static com.bol.nikhil.mancala.util.Constants.MAX_PITS;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Pit> pits ;


    @Embedded
    private Player playerOne;
    @Embedded
    private Player playerTwo;


    private Player playerActive;


    private List<Integer> housePits ;

    private GameStatus gameStatus;

    /**
     * Initialize the game.
     * Set the house pits to the middle and the end of the pits.
     * Set the pits for the game.
     * Set the playerActive and playerWaiting to null.
     * Set the gameStatus to INITIALISED.
     *
     */
    public void initialize(){
        housePits = Arrays.asList((MAX_PITS-1)/2, MAX_PITS-1);
        pits = new ArrayList<>(MAX_PITS);
        setPits();
        playerActive = null;
        gameStatus = GameStatus.INITIALISED;
    }

    /**
     * Register the user to the game
     * If both players are already registered, then throw an exception
     * If one player is already registered, then register the user as the second player
     * If no player is registered, then register the user as the first player
     * @param player
     * @throws GameException
     * @return void
     *
     */
    public void registerUser(Player player){
        if(playerOne == null){
            playerActive = player;
            playerOne = player;
        } else if (playerTwo == null){
            playerTwo = player;
        }  else {
            throw new GameException(HttpStatus.BAD_REQUEST.value() ,"Both players are already registered. Please wait or join another game.");
        }
    }

    /**
     * Start the game.
     * If both players are not registered, then throw an exception.
     * Set the gameStatus to IN_PROGRESS.
     * @throws GameException
     * @return void
     */
    public void startGame(){
        if(playerOne == null || playerTwo == null){
            throw new GameException(HttpStatus.BAD_REQUEST.value() ,"Both players are not registered. Please wait or join another game.");
        }
        gameStatus = GameStatus.IN_PROGRESS;
    }

    /**
     * set pits for the game.
     * If the pit is a house pit, then set the stone count to 0
     * If the pit is not a house pit, then set the stone count to (MAX_PITS-1)/2
     * If the pit index is less than the first house pit, then set the play turn to PLAYER_ONE
     * If the pit index is greater than the first house pit, then set the play turn to PLAYER_TWO
     *
     *
     */
    private void setPits() {
        for (int index = 0; index < MAX_PITS ; index++) {
            Pit pit = Pit.builder()
                    .index(index)
                    .build();
            if (!housePits.contains(index)) {
                pit.setStoneCount((MAX_PITS-1)/2);
            } else {
                pit.setStoneCount(0);
            }

            if(index < housePits.get(0)){
                pit.setPlayerTurn(PlayerTurn.PLAYER_ONE);
            }else{
                pit.setPlayerTurn(PlayerTurn.PLAYER_TWO);
            }

            pits.add(pit);
        }
    }


    public void makeMove(int pitId) {

        Pit pit = pits.get(pitId);
        if(pit.isOtherHousePit()){
            throw new GameException(HttpStatus.BAD_REQUEST.value() ,"Invalid move. Cannot move from other player's house");
        }
        if(pit.isOwnHousePit()){
            throw new GameException(HttpStatus.BAD_REQUEST.value() ,"Invalid move. Cannot move from own house");
        }
        int stoneCount = pit.getStoneCount();
        if(stoneCount == 0){
            throw new GameException(HttpStatus.BAD_REQUEST.value() ,"No stones present in pit. Please select another pit");
        }

        pit.setStoneCount(0);
        int nextPit = pitId ;
        for (int i = 0; i < stoneCount; i++) {
            nextPit++;
            pit = pits.get( nextPit % MAX_PITS );
            if(pit.isOtherHousePit()){
                continue;
            }
            pit.setStoneCount(pit.getStoneCount()+1);
            if (i == stoneCount-1) {
                handleLastStoneMove(nextPit);
            }

        }
        updateScores();
        checkIsGameOver();
    }

    private void updateScores() {
        int playerOneScore = pits.get(playerOne.getPlayerTurn().getHousePit()).getStoneCount();
        int playerTwoScore = pits.get(playerTwo.getPlayerTurn().getHousePit()).getStoneCount();
        playerOne.setScore(playerOneScore);
        playerTwo.setScore(playerTwoScore);
    }

    private void checkIsGameOver() {
        if(pits.stream().noneMatch(pit -> pit.isActivePlayerPit() && pit.getStoneCount() > 0)){
            List<Pit> otherPlayerPits = pits.stream().filter(pit-> !pit.isActivePlayerPit()).toList();
            Player otherPlayer = playerActive.equals(playerOne) ? playerTwo : playerOne;
            for (Pit otherPlayerPit : otherPlayerPits) {
                Pit housePit = pits.get(otherPlayer.getPlayerTurn().getHousePit());
                housePit.setStoneCount(housePit.getStoneCount() + otherPlayerPit.getStoneCount());
                otherPlayerPit.setStoneCount(0);
            }
            if (scoresAreEqual()) {
                gameStatus = GameStatus.DRAW;
            }
                else {
                    gameStatus = GameStatus.FINISHED;
                }

        }
    }

    private boolean scoresAreEqual() {
        int playerOneScore = pits.get(playerOne.getPlayerTurn().getHousePit()).getStoneCount();
        int playerTwoScore = pits.get(playerTwo.getPlayerTurn().getHousePit()).getStoneCount();
        return playerOneScore == playerTwoScore;

    }

    private void handleLastStoneMove(int pitId) {

        Pit pit = pits.get(pitId);
        if(pit.getStoneCount() == 1){
            if(pit.isActivePlayerPit()){
                Pit oppositePit = pits.get(MAX_PITS - pitId -2 );
                if(oppositePit.getStoneCount() > 0){
                    Pit housePit = pits.get(playerActive.getPlayerTurn().getHousePit());
                    housePit.setStoneCount(housePit.getStoneCount() + oppositePit.getStoneCount() + pit.getStoneCount());
                    oppositePit.setStoneCount(0);
                    pit.setStoneCount(0);
                }

            }
        }
         if (playerActive.getPlayerTurn().getHousePit() != pitId){
             setOtherPlayerActive();
        }

    }

    private void setOtherPlayerActive() {
        if( playerActive !=null &&  playerActive.equals(playerOne)){
            playerActive = playerTwo;
        }else if(playerActive !=null &&  playerActive.equals(playerTwo)){
            playerActive = playerOne;
        }
    }

    public boolean isPlayerTurn(User user) {
       return getPlayerActive().getUserId().equals(user.getId());
    }


}
