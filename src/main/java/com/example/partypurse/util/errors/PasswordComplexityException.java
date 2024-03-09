package com.example.partypurse.util.errors;

public class PasswordComplexityException extends RuntimeException{
    public PasswordComplexityException() {
        super("Password does not meet complexity requirements.");
    }

    public PasswordComplexityException(String message) {
        super(message);
    }

    public PasswordComplexityException(String message, Throwable cause) {
        super(message, cause);
    }
}
