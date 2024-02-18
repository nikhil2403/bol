package com.bol.nikhil.mancala.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Player {

    private Long userId;

    private int score;
    private PlayerTurn playerTurn;


}
