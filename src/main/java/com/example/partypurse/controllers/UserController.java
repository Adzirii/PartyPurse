package com.example.partypurse.controllers;

import com.example.partypurse.dto.request.PasswordUpdateForm;
import com.example.partypurse.dto.request.UserUpdateDto;
import com.example.partypurse.dto.response.UserInfoDto;
import com.example.partypurse.models.Room;
import com.example.partypurse.models.User;
import com.example.partypurse.services.CustomUserDetails;
import com.example.partypurse.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<UserInfoDto> info(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getInfo(user));
    }
// this is a comment

    @DeleteMapping()
    public ResponseEntity<String> delete(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletResponse response) throws IOException {
        userService.delete(userDetails);
        log.info("пользователь удален");
        return ResponseEntity.ok("Пользователь удален");
    }

    //Only user with DELETE_PRIVILEGE can do it
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.ok("Пользователь удален - admin");
    }


    // TODO: Починить обновление jwt после обновления информации о пользователе
    @PatchMapping()
    public ResponseEntity<UserInfoDto> update(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @RequestBody @Valid UserUpdateDto updateForm) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.update(updateForm, userDetails));
    }

    @PatchMapping("/passwordUpdate")
    public void passwordUpdate(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @RequestBody PasswordUpdateForm passwordUpdateForm,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) throws IOException {

        userService.passwordUpdate(userDetails, passwordUpdateForm);
        log.info("Пароль успешно изменен");
        response.sendRedirect("/api/v1/auth/logout");
    }


    // Only with READ_PRIVILEGE
    @GetMapping("/all")
    public ResponseEntity<List<UserInfoDto>> getAllUsers() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getAllUsers());
    }

    @GetMapping("/createdRooms")
    public ResponseEntity<List<Room>> userRooms(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getAllRooms(userDetails));
    }
}
