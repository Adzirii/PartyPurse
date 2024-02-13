package com.example.partypurse.dto.request;


public record SignUpRequest(String username, String firstName, String lastName, String password, String confirmPassword) {
}