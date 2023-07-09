package com.poster.exception;

public class UserActionRestrictedException extends RuntimeException {
    public UserActionRestrictedException(String message) {
        super(message);
    }
}
