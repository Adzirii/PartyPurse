package com.example.partypurse.dto.request;

import java.io.Serializable;

/**
 * DTO for {@link com.example.partypurse.models.User}
 */
public record SignUpRequest(String username, String firstName, String lastName, String password, String confirmPassword) implements Serializable {
}