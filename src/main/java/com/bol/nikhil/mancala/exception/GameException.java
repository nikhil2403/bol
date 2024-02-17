package com.bol.nikhil.mancala.exception;


import lombok.Data;

@Data
public class GameException extends  RuntimeException {
    private int code;
    private String message;
    public  GameException(int code,String message){
        super(message);
        this.code = code;
        this.message = message;
    }
}