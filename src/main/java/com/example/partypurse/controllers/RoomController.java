package com.example.partypurse.controllers;

import com.example.partypurse.dto.request.RoomCreationForm;
import com.example.partypurse.dto.request.RoomUpdateForm;
import com.example.partypurse.services.CustomUserDetails;
import com.example.partypurse.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<String> createRoom(@RequestBody RoomCreationForm form, @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok("Room link = " + roomService.save(form, userDetails));
    }


    @PostMapping("/{id}/update")
    public ResponseEntity<String> updateRoom(@PathVariable Long id, @RequestBody RoomUpdateForm form, @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(roomService.update(id, form, userDetails));
    }

    @GetMapping("/{id}/delete")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(roomService.delete(id, userDetails));
    }

    @GetMapping("/roomsInfo")
    public ResponseEntity<?> userRooms(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(roomService.getAllUserRooms(userDetails));
    }

    @GetMapping("/{id}/{type}") //TODO: в будущем возможен отказ, от этого.
    public ResponseEntity<?> roomAction(@PathVariable Long id, @PathVariable String type,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return switch (type) {
            case "info" -> roomService.getSingleRoomInfo(id, userDetails);
            case "participants" -> roomService.getAllRoomParticipants(id, userDetails);
            case "products" -> roomService.getAllRoomProducts(id, userDetails);
            case "totalPrice" -> roomService.getTotalPrice(id, userDetails);
            default -> ResponseEntity.badRequest().body("Invalid room action type");
        };
    }


    //    @GetMapping("/{id}/info")
//    public ResponseEntity<?> roomInfo(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails){
//        return roomService.getSingleRoomInfo(id, userDetails);
//    }
//
//    @GetMapping("/{id}/participants")
//    public ResponseEntity<?> roomParticipants(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails){
//        return roomService.getAllRoomParticipants(id, userDetails);
//    }
//
//    @GetMapping("/{id}/products")
//    public ResponseEntity<?> roomProducts(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails){
//        return roomService.getAllRoomProducts(id, userDetails);
//    }
//
//    @GetMapping("/{id}/totalPrice")
//    public ResponseEntity<?> roomTotalPrice(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails){
//        return roomService.getTotalPrice(id, userDetails);
//    }
    @GetMapping("join/{link}")
    public ResponseEntity<String> joinRoom(@PathVariable String link, @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(roomService.join(link, userDetails));
    }

    @GetMapping("/{id}/leave")
    public ResponseEntity<String> leaveRoom(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(roomService.leave(id, userDetails));
    }

    @GetMapping("/{roomId}/kickUser/{userId}")
    public ResponseEntity<String> kickUser(@PathVariable Long roomId, @PathVariable Long userId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(roomService.kickUser(roomId, userId, userDetails));
    }

    @GetMapping("/getLink/{id}")
    public ResponseEntity<?> getInviteLink(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(roomService.getInviteLink(id, userDetails));
    }

}
