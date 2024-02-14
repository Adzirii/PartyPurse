package com.example.partypurse.controllers;

import com.example.partypurse.dto.request.SignInRequest;
import com.example.partypurse.dto.request.SignUpRequest;
import com.example.partypurse.services.AuthService;
import com.example.partypurse.services.InMemoryTokenBlacklist;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthService authService;
    private final InMemoryTokenBlacklist tokenBlacklist;

    @PostMapping("/singup")
    public ResponseEntity<?> register(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authService.registerNewUser(signUpRequest));
    }

    @PostMapping("/singin")
    public ResponseEntity<?> login(@RequestBody SignInRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        tokenBlacklist.addToBlacklist(token);

        // Clear any session-related data if necessary

        return ResponseEntity.ok("Logged out successfully");
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        // Get the Authorization header from the request
        String authorizationHeader = request.getHeader("Authorization");

        // Check if the Authorization header is not null and starts with "Bearer "
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            // Extract the JWT token (remove "Bearer " prefix)
            return authorizationHeader.substring(7);
        }

        // If the Authorization header is not valid, return null
        return null;
    }
}
