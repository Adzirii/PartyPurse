package com.example.partypurse.controllers;

import com.example.partypurse.dto.request.RoomCreationForm;
import com.example.partypurse.services.JwtService;
import com.example.partypurse.services.RoomService;
import com.example.partypurse.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<String> createRoom(@RequestBody RoomCreationForm form, @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok("Room link = " + roomService.save(form, userDetails));
    }

    @GetMapping("join/{link}")
    public ResponseEntity<String> joinRoom(@PathVariable String link, @AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity.ok(roomService.join(link, userDetails));
    }

    @GetMapping("/getLink/{id}")
    public ResponseEntity<?> getInviteLink(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(roomService.getInviteLink(id, userDetails));
    }
}
