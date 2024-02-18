package com.bol.nikhil.mancala.controller;

import com.bol.nikhil.mancala.model.Game;
import com.bol.nikhil.mancala.model.GameResult;
import com.bol.nikhil.mancala.model.User;
import com.bol.nikhil.mancala.service.GameService;
import com.bol.nikhil.mancala.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.*;

//@SpringBootTest
//@AutoConfigureMockMvc
class GameControllerTest {

     @Autowired
    private MockMvc mockMvc;

     //mock GameService and UserService
     @MockBean
     private GameService gameService;

    @MockBean
    private UserService userService;

    @InjectMocks
    private GameController gameController;


    @Autowired
    private ObjectMapper objectMapper;


    //@Test
    void createGame() throws Exception {
        //create mock mvc test for createGame.
        mockMvc.perform(post("/games/createGame"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists());
    }

  //  @Test
    void startGame() throws Exception {
        //create mock mvc test for startGame.
       when(gameService.startGame(any(Long.class))).thenReturn(Game.builder().id(1L).build());

        mockMvc.perform(put("/games/startGame/{id}", 1L))
                .andExpect(status().isOk());

        verify(gameService, times(1)).startGame(1L);
    }

  //  @Test
    void getGame() throws Exception {
        //create mock mvc test for getGame -  /games/{gameId}
        when(gameService.getGame(any(Long.class))).thenReturn(Game.builder().id(1L).build());

        mockMvc.perform(get("/games/{id}", 1L))
                .andExpect(status().isOk());

        verify(gameService, times(1)).getGame(1L);

    }

  //  @Test
    void registerUser() throws Exception {
        User user = new User();
        user.setName("Test User");

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        verify(userService, times(1)).createUser(any(User.class));
    }

  //  @Test
    void makeMove() throws Exception {
        when(gameService.makeMove(any(Long.class),any(),any())).thenReturn(Game.builder().id(1L).build());

        mockMvc.perform(get("/playmove/{gameId}/pits/{pitId}/user/{userId}", 1L,1, 1L))
                .andExpect(status().isOk());

        verify(gameService, times(1)).getGame(1L);
    }

  //  @Test
    void getGameStats() throws Exception {
        //create mock mvc test for getGameStats
        when(gameService.getGameStats(any(Long.class))).thenReturn(GameResult.builder().build());

        mockMvc.perform(get("/gamestats/{gameId}", 1L))
                .andExpect(status().isOk());

        verify(gameService, times(1)).getGame(1L);

    }
}