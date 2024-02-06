package com.example.partypurse.controllers;

import com.example.partypurse.models.User;
import com.example.partypurse.repositories.UserRepository;
import com.example.partypurse.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@Slf4j
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("hi1")
    public String hi1(){
        return "Auth-User"; // -
    }
    @GetMapping("hi2")
    public String hi2(){
        return "Auth-Admin"; //-
    }
    @GetMapping("hi3")
    public String hi3(){
        return "Role-User"; //-
    }
    @GetMapping("hi4")
    public String hi4(){
        return "Role-Admin";
    }
    @GetMapping("hi5")
    public String hi5(){
        return "Auth";
    }

    @GetMapping("/info")
    public String info(@AuthenticationPrincipal UserDetails userDetails){
        log.info(userDetails.getUsername());
        log.info(userDetails.getAuthorities().stream().map(auth -> auth).toString());
        return String.format("username = %s, getAuthorities = %s, password = %s",
                userDetails.getUsername(),userDetails.getAuthorities(),userDetails.getPassword());
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserDetails userDetails){
        User user = userService.findByUsername(userDetails.getUsername()).get();
        userService.delete(user);
        return ResponseEntity.ok("Пользователь удален");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("ADMIN")
    public ResponseEntity<String> deleteByAdmin(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("Пользователь удален");
    }
}
