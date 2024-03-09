package com.example.partypurse.dto.response;

public record UserInfoDto(Long id, String username, String firstName, String lastName,
                          java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> roles) {
}