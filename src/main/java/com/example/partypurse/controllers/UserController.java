package com.example.partypurse.controllers;

import com.example.partypurse.dto.response.UserDto;
import com.example.partypurse.models.User;
import com.example.partypurse.services.RoomService;
import com.example.partypurse.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RoomService roomService;


    @GetMapping("/info")
    public ResponseEntity<UserDto> info(@AuthenticationPrincipal UserDetails userDetails){
        User user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(userService.getInfo(user));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserDetails userDetails){
        User user = userService.findByUsername(userDetails.getUsername());
        userService.delete(user);
        return ResponseEntity.ok("Пользователь удален");
    }

    @GetMapping("/role")
    public ResponseEntity<?> getRoles(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(userDetails.getAuthorities());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("ADMIN")
    public ResponseEntity<String> deleteByAdmin(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("Пользователь удален");
    }

    @GetMapping("/roomsInfo")
    public ResponseEntity<?> userRooms(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(roomService.getAllUserRooms(userDetails));
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
