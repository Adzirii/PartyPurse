package com.example.partypurse.services;

import com.example.partypurse.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final String password;
    private final String username;
    private final Collection<? extends GrantedAuthority> authorities;
    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        this.password = user.getPassword();
        this.username = user.getUsername();
        this.authorities = authorities;
    }

    // Hello

    ///asdf

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
