package com.example.partypurse.util.errors;

public class PasswordNotEqualsException extends RuntimeException{
    public PasswordNotEqualsException() {
        super("Password not equal!");
    }

    public PasswordNotEqualsException(String message) {
        super(message);
    }

    public PasswordNotEqualsException(String message, Throwable cause) {
        super(message, cause);
    }
}
