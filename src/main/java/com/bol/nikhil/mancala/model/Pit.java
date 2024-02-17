package com.bol.nikhil.mancala.model;

import com.bol.nikhil.mancala.util.Constants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Pit implements Comparable<Pit> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer index;
    private int stoneCount;
    private PlayerTurn playerTurn;


    public void setStoneCount(int stoneCount) {
        this.stoneCount = stoneCount;
    }

    public boolean isOwnHousePit(){
        return  this.index == playerTurn.getHousePit() ;
    }


    /**
     * if player one is playing then he is not allowed to put stone in opposition's house
     * and vice versa from player two
     *
     */
    public boolean isOtherHousePit(){
        return PlayerTurn.PLAYER_TWO.equals(playerTurn) && this.index == PlayerTurn.PLAYER_ONE.getHousePit()
                || PlayerTurn.PLAYER_ONE.equals(playerTurn) && this.index == PlayerTurn.PLAYER_TWO.getHousePit();
    }


    public boolean isActivePlayerPit(){
        return  getPlayerTurn().housePit == (Constants.MAX_PITS - 1) / 2 && (index < getPlayerTurn().housePit && index > 0)
                || getPlayerTurn().housePit == Constants.MAX_PITS-1 && (index < getPlayerTurn().housePit && index > (Constants.MAX_PITS - 1) / 2);

    }

    @Override
    public int compareTo(Pit pit) {
        return this.getIndex().compareTo(pit.getIndex());
    }
}
