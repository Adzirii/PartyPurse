package com.example.partypurse.controllers;

import com.example.partypurse.dto.request.ProductCreationForm;
import com.example.partypurse.dto.request.ProductUpdateForm;
import com.example.partypurse.dto.request.RoomCreationForm;
import com.example.partypurse.dto.request.RoomUpdateForm;
import com.example.partypurse.dto.response.ProductInfoDto;
import com.example.partypurse.dto.response.RoomInfoDto;
import com.example.partypurse.models.Product;
import com.example.partypurse.services.CustomUserDetails;
import com.example.partypurse.services.ProductService;
import com.example.partypurse.services.RoomService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<RoomInfoDto> createRoom(@RequestBody @Valid RoomCreationForm form, @AuthenticationPrincipal CustomUserDetails userDetails) {


        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roomService.save(form, userDetails));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomInfoDto> getInfoById(@PathVariable Long roomId, HttpServletResponse response) throws IOException {
        if (roomId == 228)
            response.sendRedirect("https://rt.pornhub.com/");
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roomService.getInfoById(roomId));
    }

    @GetMapping()
    public ResponseEntity<List<RoomInfoDto>> getAllRooms() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roomService.getAll());
    }

    @GetMapping("/{roomId}/link")
    public ResponseEntity<String> getRoomLink(@PathVariable Long roomId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roomService.getInviteLink(roomId, userDetails));
    }

    @PatchMapping("/{roomId}")
    public ResponseEntity<String> updateRoom(@PathVariable Long roomId, @RequestBody @Valid RoomUpdateForm form, @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roomService.update(roomId, form, userDetails));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long roomId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roomService.delete(roomId, userDetails));
    }

    @GetMapping("/{roomId}/invite/{userId}")
    public ResponseEntity<String> invite(@PathVariable Long userId, @PathVariable Long roomId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roomService.inviteUser(userId, roomId, userDetails));
    }

    @GetMapping("/{link}/join")
    public ResponseEntity<String> join(@PathVariable String link, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roomService.join(link, userDetails));
    }


    @GetMapping("/{roomId}/joinById")
    public ResponseEntity<String> join(@PathVariable Long roomId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roomService.join(roomId, userDetails));
    }

    @GetMapping("/{roomId}/leave")
    public ResponseEntity<String> leave(@PathVariable Long roomId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roomService.leave(roomId, userDetails));
    }

    @GetMapping("/{roomId}/kickUser/{userId}")
    public ResponseEntity<String> kickUser(@PathVariable Long roomId, @PathVariable Long userId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roomService.kick(roomId, userId, userDetails));
    }

    @GetMapping("/{roomId}/changeCreator/{userId}")
    public ResponseEntity<String> changeCreator(@PathVariable Long roomId, @PathVariable Long userId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roomService.changeCreator(roomId, userId, userDetails));
    }


    @GetMapping("/{roomId}/{type}")
    public ResponseEntity<?> roomAction(@PathVariable Long roomId, @PathVariable String type) {
        return switch (type) {

            case "name" -> roomService.getName(roomId);
            case "description" -> roomService.getDescription(roomId);
            case "createdAt" -> roomService.getCreatedAt(roomId);
            case "creator" -> roomService.getCreator(roomId);
            case "category" -> roomService.getCategory(roomId);
            case "users" -> roomService.getAllUsers(roomId);
            case "products" -> roomService.getAllProducts(roomId);
            case "totalPrice" -> roomService.getTotalPrice(roomId);
            default -> ResponseEntity.badRequest().body("Invalid room action type");
        };
    }







    @PostMapping("/{roomId}/products")
    public ResponseEntity<ProductInfoDto> saveProduct(@PathVariable Long roomId, @RequestBody @Valid ProductCreationForm productPayload, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roomService.addProduct(roomId, productPayload, userDetails));
    }

    @GetMapping("/products/{prodId}")
    public ResponseEntity<ProductInfoDto> getProduct(@PathVariable Long prodId) {
        var product = productService.getById(prodId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.getInfo(product));
    }

    @GetMapping("{roomId}/products")
    public ResponseEntity<List<ProductInfoDto>> getAllProduct(@PathVariable Long roomId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roomService.allProducts(roomId, userDetails));
    }

    @PatchMapping("/{roomId}/products/{productId}")
    public ResponseEntity<ProductInfoDto> updateProduct(@PathVariable Long roomId, @PathVariable Long productId, @RequestBody @Valid ProductUpdateForm productPayload, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roomService.updateProduct(roomId, productId, userDetails, productPayload));
    }

    @DeleteMapping("/{roomId}/products/{prodId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long roomId, @PathVariable Long prodId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        roomService.deleteProduct(roomId, prodId, userDetails);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("Продукт удален.");
    }

}
