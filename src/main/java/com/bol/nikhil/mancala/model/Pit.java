package com.bol.nikhil.mancala.model;

import com.bol.nikhil.mancala.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Pit  {

    private Integer index;
    private int stoneCount;
    private PlayerTurn playerOwningThisPit;


    public void setStoneCount(int stoneCount) {
        this.stoneCount = stoneCount;
    }

    public boolean isOwnHousePit(){
        return  this.index == playerOwningThisPit.getHousePit() ;
    }


    /**
     * if player one is playing then he is not allowed to put stone in opposition's house
     * and vice versa from player two
     *
     */
    public boolean isOtherHousePit(){
        return PlayerTurn.PLAYER_TWO.equals(playerOwningThisPit) && this.index == PlayerTurn.PLAYER_ONE.getHousePit()
                || PlayerTurn.PLAYER_ONE.equals(playerOwningThisPit) && this.index == PlayerTurn.PLAYER_TWO.getHousePit();
    }


}
