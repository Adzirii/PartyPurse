package com.example.partypurse.dto.response;

import com.example.partypurse.models.ERole;

import java.io.Serializable;

/**
 * DTO for {@link com.example.partypurse.models.Role}
 */
public record RoleDto(Long id, String name) implements Serializable {
}