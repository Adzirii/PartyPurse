package com.example.partypurse.util.errors;

public class UserAuthorityException extends RuntimeException{
    public UserAuthorityException(String errorMessage) {
        super(errorMessage);
    }
}
