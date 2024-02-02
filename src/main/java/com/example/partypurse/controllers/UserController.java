package com.example.partypurse.controllers;

import com.example.partypurse.models.User;
import com.example.partypurse.repositories.UserRepository;
import com.example.partypurse.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserDetails userDetails){
        User user = userService.findByUsername(userDetails.getUsername()).get();
        userService.delete(user);
        return ResponseEntity.ok("Пользователь удален");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteByAdmin(@PathVariable Long id){
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователя с таким id не найден"));
        userService.delete(user);
        return ResponseEntity.ok("Пользователь удален");
    }
}
