package com.example.partypurse.controllers;

import com.example.partypurse.dto.request.SignInRequest;
import com.example.partypurse.dto.request.SignUpRequest;
import com.example.partypurse.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthService authService;

    @PostMapping("/singup")
    public ResponseEntity<?> register(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authService.registerNewUser(signUpRequest));
    }

    @PostMapping("/singin")
    public ResponseEntity<?> login(@RequestBody SignInRequest loginRequest){
        return authService.login(loginRequest);
    }
}
