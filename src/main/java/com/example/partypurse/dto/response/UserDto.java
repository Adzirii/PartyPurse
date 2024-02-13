package com.example.partypurse.dto.response;

import java.io.Serializable;

/**
 * DTO for {@link com.example.partypurse.models.User}
 */
public record UserDto(Long id, String username, String firstName, String lastName,
                      java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> roles) implements Serializable {
}