package com.bol.nikhil.mancala.dto;


import com.bol.nikhil.mancala.model.Game;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GameMoveResponse {
    private Game game;
    private Map<String,String> status ;
}