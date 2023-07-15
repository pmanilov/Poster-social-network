package com.poster.exception;

public class ChatAlreadyCreated extends RuntimeException{
    public ChatAlreadyCreated(String message) {
        super(message);
    }
}
