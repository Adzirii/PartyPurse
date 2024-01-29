package com.example.partypurse.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.example.partypurse.models.User}
 */
public record UserClaims(Long id, String username, String firstName, String lastName) implements Serializable {
}