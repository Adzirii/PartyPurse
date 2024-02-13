package com.example.partypurse.dto.response;

import com.example.partypurse.models.Privilege;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.example.partypurse.models.Role}
 */
public record RoleDto(Long id, String name, Set<Privilege> privileges) implements Serializable {
}