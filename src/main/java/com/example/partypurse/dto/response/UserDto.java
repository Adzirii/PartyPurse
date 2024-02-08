package com.example.partypurse.dto.response;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.example.partypurse.models.User}
 */
public record UserDto(Long id, String username, String firstName, String lastName,
                      Set<RoleDto> roles) implements Serializable {
}