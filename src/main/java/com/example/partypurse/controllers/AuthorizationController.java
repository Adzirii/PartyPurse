package com.example.partypurse.controllers;

import com.example.partypurse.dto.request.SignInRequest;
import com.example.partypurse.dto.request.SignUpRequest;
import com.example.partypurse.dto.response.JwtResponse;
import com.example.partypurse.dto.response.UserInfoDto;
import com.example.partypurse.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserInfoDto> register(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(authService.registerNewUser(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> login(@RequestBody SignInRequest loginRequest){
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(authService.login(loginRequest));
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
