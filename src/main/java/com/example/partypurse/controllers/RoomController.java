package com.example.partypurse.controllers;

import com.example.partypurse.dto.request.RoomCreationForm;
import com.example.partypurse.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;


    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestBody RoomCreationForm form, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(roomService.save(form, userDetails));
    }
}
