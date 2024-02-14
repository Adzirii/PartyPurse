package com.example.partypurse.controllers;

import com.example.partypurse.dto.request.UserUpdateRequest;
import com.example.partypurse.dto.response.UserDto;
import com.example.partypurse.models.User;
import com.example.partypurse.services.CustomUserDetails;
import com.example.partypurse.services.RoomService;
import com.example.partypurse.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<?> info(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(userService.getInfo(user));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        userService.delete(user);
        return ResponseEntity.ok("Пользователь удален");
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteByAdmin(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Пользователь удален - admin");
    }


    @PostMapping("/update")
    public ResponseEntity<String> deleteByAdmin(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @RequestBody UserUpdateRequest updateForm) {

        return ResponseEntity.ok("пользователь обновлен: " + userService.update(updateForm, userDetails));
    }



    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
