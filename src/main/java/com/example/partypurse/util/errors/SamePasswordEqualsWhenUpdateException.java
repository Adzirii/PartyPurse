package com.example.partypurse.util.errors;

public class SamePasswordEqualsWhenUpdateException extends RuntimeException{
    public SamePasswordEqualsWhenUpdateException() {
        super("Password is equal to the previous one");
    }

    public SamePasswordEqualsWhenUpdateException(String message) {
        super(message);
    }

    public SamePasswordEqualsWhenUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
