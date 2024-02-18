package com.bol.nikhil.mancala.model;

import com.bol.nikhil.mancala.exception.GameException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import static com.bol.nikhil.mancala.util.Constants.MAX_PITS;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {

    private Long id;

    private List<Pit> pits ;



    private Player playerOne;

    private Player playerTwo;


    private Player playerActive;


    private List<Integer> housePits ;

    private GameStatus gameStatus;

    private ReentrantLock lock ;

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
        gameStatus = GameStatus.INITIALIZED;
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
            player.setPlayerTurn(PlayerTurn.PLAYER_ONE);
        } else if (playerTwo == null){
            playerTwo = player;
            player.setPlayerTurn(PlayerTurn.PLAYER_TWO);
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

            if(index <= housePits.get(0)){
                pit.setPlayerOwningThisPit(PlayerTurn.PLAYER_ONE);
            }else{
                pit.setPlayerOwningThisPit(PlayerTurn.PLAYER_TWO);
            }

            pits.add(pit);
        }
    }


    public void makeMove(int pitId) {
        //throw exception if pitId is not valid
        if(pitId < 0 || pitId >= MAX_PITS){
            throw new GameException(HttpStatus.BAD_REQUEST.value() ,"Invalid pitId. Please enter valid pitId");
        }

        Pit pit = pits.get(pitId);
        if(pit.isOtherHousePit()){
            throw new GameException(HttpStatus.BAD_REQUEST.value() ,"Invalid move. Cannot move from other player's house");
        }
        if(pit.isOwnHousePit()){
            throw new GameException(HttpStatus.BAD_REQUEST.value() ,"Invalid move. Cannot move from own house");
        }

        // throw exception if  pit is not active player's pit
        if(!isActivePlayerPit(pit)){
            throw new GameException(HttpStatus.BAD_REQUEST.value() ,"Invalid move. Not active player's pit");
        }

        //lock the game to avoid concurrent moves.
        try {
            lock.lock();
            int stoneCount = pit.getStoneCount();
            if (stoneCount == 0) {
                throw new GameException(HttpStatus.BAD_REQUEST.value(), "No stones present in pit. Please select another pit");
            }

            pit.setStoneCount(0);
            int nextPit = pitId;
            for (int i = 0; i < stoneCount; i++) {
                nextPit++;
                nextPit = nextPit % MAX_PITS;

                pit = pits.get(nextPit);
                if (pit.isOtherHousePit()) {
                    i--;
                    continue;
                }
                pit.setStoneCount(pit.getStoneCount() + 1);
                if (i == stoneCount - 1) {
                    handleLastStoneMove(nextPit);
                }

            }
            updateScores();
            checkIsGameOver();
            if (playerActive.getPlayerTurn().getHousePit() != pitId) {
                setOtherPlayerActive();
            }
        }finally {
            lock.unlock();
        }
    }

    /**
     * Update the scores of the players.
     * Set the playerOne score to the stone count of the playerOne's house pit.
     * Set the playerTwo score to the stone count of the playerTwo's house pit.
     * @return void
     */

    private void updateScores() {
        int playerOneScore = pits.get(playerOne.getPlayerTurn().getHousePit()).getStoneCount();
        int playerTwoScore = pits.get(playerTwo.getPlayerTurn().getHousePit()).getStoneCount();
        playerOne.setScore(playerOneScore);
        playerTwo.setScore(playerTwoScore);
    }

    /**
     * Check if the game is over.
     * Game is over when all the pits of active player are empty, except the house pit .
     * If the game is over, then move all the stones from other player's owned pits to his house pit.
     * If the game is over, then set the gameStatus to FINISHED.
     * If the scores are equal, then set the gameStatus to DRAW.
     * @return void
     */

    private void checkIsGameOver() {
        if(pits.stream().noneMatch(pit -> isActivePlayerPit(pit) && !housePits.contains(pit.getIndex()) && pit.getStoneCount() > 0)){
            List<Pit> otherPlayerPits = pits.stream().filter(pit-> !isActivePlayerPit(pit)).toList();
            Player otherPlayer = getOtherPlayer();
            Pit housePit = pits.get(otherPlayer.getPlayerTurn().getHousePit());
            for (Pit otherPlayerPit : otherPlayerPits) {
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

    private Player getOtherPlayer() {
        return playerActive.equals(playerOne) ? playerTwo : playerOne;
    }

    /**
     * Check if the scores are equal.
     * If the scores are equal, then the game is a draw.
     * @return boolean
     */
    private boolean scoresAreEqual() {
        int playerOneScore = pits.get(playerOne.getPlayerTurn().getHousePit()).getStoneCount();
        int playerTwoScore = pits.get(playerTwo.getPlayerTurn().getHousePit()).getStoneCount();
        return playerOneScore == playerTwoScore;

    }

    /**
     * Handle the last stone move.
     * If the last stone is moved to the active player's house pit, then the active player gets another turn.
     * If the last stone is moved to an empty pit and that pit is active player's pit , then the active player gets all the stones from the opposite pit and the stone from the last pit.
     * If the last stone is moved to the active player's pit, then the other player becomes the active player.
     * @param pitId
     * @return void
     */
    private void handleLastStoneMove(int pitId) {

        Pit pit = pits.get(pitId);
        if(pit.getStoneCount() == 1){
            if(isActivePlayerPit(pit)){
                Pit oppositePit = pits.get(MAX_PITS - pitId -2 );
                if(oppositePit.getStoneCount() > 0){
                    Pit housePit = pits.get(playerActive.getPlayerTurn().getHousePit());
                    housePit.setStoneCount(housePit.getStoneCount() + oppositePit.getStoneCount() + pit.getStoneCount());
                    oppositePit.setStoneCount(0);
                    pit.setStoneCount(0);
                }

            }
        }


    }

    /**
     * Check if the pit is an active player's pit.
     * If the active player's house pit is the first house pit, then the pit is an active player's pit if the pit index is less than the active player's house pit and greater than 0.
     * If the active player's house pit is the second house pit, then the pit is an active player's pit if the pit index is less than the active player's house pit and greater than the first house pit.
     * @param pit
     * @return
     */
    private boolean isActivePlayerPit(Pit pit) {
        return  playerActive.getPlayerTurn().getHousePit() == housePits.get(0)
                && (pit.getIndex() <=playerActive.getPlayerTurn().getHousePit() && pit.getIndex() >= 0)
                ||
                playerActive.getPlayerTurn().getHousePit() == housePits.get(1)
                && (pit.getIndex() <=playerActive.getPlayerTurn().getHousePit() && pit.getIndex() > housePits.get(0));
    }

    /**
     * Set the other player as the active player.
     * If the active player is playerOne, then set the active player to playerTwo.
     * If the active player is playerTwo, then set the active player to playerOne.
     * @return void
     */
    private void setOtherPlayerActive() {
        if( playerActive !=null &&  playerActive.equals(playerOne)){
            playerActive = playerTwo;
        }else if(playerActive !=null &&  playerActive.equals(playerTwo)){
            playerActive = playerOne;
        }
    }

    /**
     * Get the active player.
     * @param user
     * @return
     */
    public boolean isPlayerTurn(User user) {
       return getPlayerActive().getUserId().equals(user.getId());
    }


    public Player getWaitingPlayer() {
        return playerActive.equals(playerOne) ? playerTwo : playerOne;
    }
}
