package com.example.partypurse.controllers;

import com.example.partypurse.dto.request.SignInRequest;
import com.example.partypurse.dto.request.SignUpRequest;
import com.example.partypurse.services.AuthService;
import com.example.partypurse.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authService.registerNewUser(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody SignInRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/home")
    public ResponseEntity<String> home() {
        log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ВЫШЛИ!!!!!!!!!!!!!!!!!!!!!!!!");
        return ResponseEntity.ok("Вы вышли");
    }
}
