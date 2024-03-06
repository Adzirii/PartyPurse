package com.example.partypurse.controllers;

import com.example.partypurse.dto.request.ProductCreationForm;
import com.example.partypurse.dto.request.RoomCreationForm;
import com.example.partypurse.dto.request.RoomUpdateForm;
import com.example.partypurse.dto.response.ProductDto;
import com.example.partypurse.dto.response.RoomDto;
import com.example.partypurse.services.CustomUserDetails;
import com.example.partypurse.services.ProductService;
import com.example.partypurse.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getInfoById(@PathVariable Long id){

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body( roomService.getInfoById(id));
    }



    @PostMapping("/{id}/products")
    public ResponseEntity<String> saveProduct(@RequestBody ProductCreationForm form, @PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails){
        roomService.addProduct(id, form, userDetails);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("Продукт сохранен.");
    }

    @DeleteMapping("/{roomId}/products/{prodId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long roomId, @PathVariable Long prodId, @AuthenticationPrincipal CustomUserDetails userDetails){
        roomService.deleteProduct(roomId,prodId, userDetails);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("Продукт удален.");
    }

    // Не стал делать ограничения на получени информации о продукте
    @GetMapping("/products/{prodId}")
    public ResponseEntity<ProductDto> getProduct( @PathVariable Long prodId){
        var product = productService.getById(prodId);
        var info = productService.getInfo(product);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(info);
    }


    // ДОСТУПНО ДЛЯ ВСЕХ
    @GetMapping("/{roomId}/cost")
    public ResponseEntity<Double> getTotalPrice(@PathVariable Long roomId){
        var cost = roomService.getAllProductsCost(roomId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(cost);
    }


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

//    @GetMapping("/getAllUsers")
//    public ResponseEntity<?> userRooms(@AuthenticationPrincipal CustomUserDetails userDetails) {
//        return ResponseEntity.ok(roomService.getAllUserRooms(userDetails));
//    }


    // ИНФОРМАЦИЯ ДОСТУПАНЯ ДЛЯ ВСЕХ. userDetails можно удалить.
    @GetMapping("/{id}/{type}")
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

    //ДОСТУПНО ТОЛЬКО СОЗДЕТЛЮ КОМНАТЫ
    @GetMapping("/getLink/{id}")
    public ResponseEntity<?> getInviteLink(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(roomService.getInviteLink(id, userDetails));
    }

}
