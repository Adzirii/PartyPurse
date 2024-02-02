package com.example.partypurse.dto.request;

import java.io.Serializable;


public record SignUpRequest(String username, String firstName, String lastName, String password, String confirmPassword) {
}