package com.example.partypurse.util.errors;

public class ProductModifyException extends RuntimeException{
    public ProductModifyException() {
        super("You have no right to modify this product.");
    }

    public ProductModifyException(String message) {
        super(message);
    }

    public ProductModifyException(String message, Throwable cause) {
        super(message, cause);
    }
}
