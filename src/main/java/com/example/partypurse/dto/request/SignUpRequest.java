package com.example.partypurse.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank
        @Size(min = 4, max = 30)
        String username,
        @NotBlank
        @Size(min = 4, max = 30)
        String firstName,
        @NotBlank
        @Size(min = 4, max = 30) String lastName,
        String password, String confirmPassword) {
}