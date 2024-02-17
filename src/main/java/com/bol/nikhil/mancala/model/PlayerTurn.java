package com.bol.nikhil.mancala.model;

import com.bol.nikhil.mancala.util.Constants;

public enum PlayerTurn {
    PLAYER_ONE((Constants.MAX_PITS - 1) / 2),

    PLAYER_TWO( Constants.MAX_PITS-1);

    int housePit;

    PlayerTurn(int houseIndex) {
        this.housePit = houseIndex;
    }

    public int getHousePit() {
        return housePit;
    }



}
