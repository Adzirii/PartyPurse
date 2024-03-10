package com.example.partypurse.util.errors;

public class RoomAccessException extends RuntimeException{
    public RoomAccessException() {
        super("You have no right to change this room.");
    }

    public RoomAccessException(String message) {
        super(message);
    }

    public RoomAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
