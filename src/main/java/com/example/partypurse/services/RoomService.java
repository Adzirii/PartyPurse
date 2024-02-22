package com.example.partypurse.services;

import com.example.partypurse.dto.request.RoomCreationForm;
import com.example.partypurse.dto.request.RoomUpdateForm;
import com.example.partypurse.dto.response.ProductDto;
import com.example.partypurse.dto.response.RoomDto;
import com.example.partypurse.dto.response.UserDto;
import com.example.partypurse.models.Room;
import com.example.partypurse.models.User;
import com.example.partypurse.repositories.RoomRepository;
import com.example.partypurse.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserService userService;
    private final ProductService productService;
    private final LinkService linkService;
    private final UserRepository userRepository;

    public Room findRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Такой комнаты не существует"));
    }

    public Room findByLink(String link) {
        return roomRepository.findByInvitationLink(link).orElseThrow(() -> new IllegalArgumentException("Комнаты с такой ссылкой не существует"));
    }


    public String join(String inviteLink, UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Room room = findByLink(inviteLink);
        if (room.getUsers().contains(user))
            return "Вы уже являетесь участником";
        room.getUsers().add(user);
        user.getVisitedRooms().add(room);
        roomRepository.save(room);
        userRepository.save(user);
        return "Вы успешно вошли";
    }

    public String leave(Long id, UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Room room = findRoomById(id);
        if (!room.getUsers().contains(user))
            return "Вы уже не являетесь участником";
        room.getUsers().remove(user);
        user.getVisitedRooms().remove(room);
        roomRepository.save(room);
        userRepository.save(user);
        return "Вы успешно вышли из комнаты";
    }


    public String kickUser(Long roomId, Long userId, CustomUserDetails userDetails) {
        User user = userService.findById(userId);
        Room room = findRoomById(roomId);
        if (checkRoomCreator(room, userDetails))
            return "Только создатель группы может это сделать";
        room.getUsers().remove(user);
        user.getVisitedRooms().remove(room);
        roomRepository.save(room);
        userRepository.save(user);
        return "Пользователь исключен";
    }

    public String getInviteLink(Long id, UserDetails userDetails) {
        Room room = findRoomById(id);
        if (checkRoomCreator(room, userDetails))
            return "Только создатель комнаты может получить ссылку-приглашение";
        return room.getInvitationLink();
    }

    public RoomDto getInfo(String link) {
        Room room = findByLink(link);
        List<UserDto> users = room.getUsers().stream().map(userService::getInfo).toList();
        List<ProductDto> products = room.getProducts().stream().map(productService::getInfo).toList();
        return new RoomDto(room.getId(), room.getName(), userService.getInfo(room.getCreator()), room.getInvitationLink(), room.getDescription(),
                room.getCreatedAt(), room.getRoomCategory(), users, products);
    }

    //TODO: Сделать roomUpdate

    public List<RoomDto> getAllUserRooms(UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        return user.getCreatedRooms().stream().map(room -> getInfo(room.getInvitationLink())).toList();
    }

    private boolean checkRoomCreator(Room room, UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        return !room.getCreator().getId().equals(user.getId());
    }

//    public ResponseEntity<?> getSingleRoomInfo(Long id, UserDetails userDetails){
//        Room room = findRoomById(id);
//
//        if (checkRoomCreator(room, userDetails))
//            return ResponseEntity.badRequest()
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body("Вы не можете получить доступ к чужой комнате");
//
//        return ResponseEntity.ok(getInfo(room.getInvitationLink()));
//    }
//
//    public ResponseEntity<?> getAllRoomParticipants(Long id, UserDetails userDetails){
//        Room room = findRoomById(id);
//        if (checkRoomCreator(room, userDetails))
//            return ResponseEntity.badRequest()
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body("Вы не можете получить доступ к информации о чужой комнате");
//        List<UserDto> users = room.getUsers().stream().map(userService::getInfo).toList();
//        return ResponseEntity.ok(users);
//
//    }
//
//    public ResponseEntity<?> getAllRoomProducts(Long id, UserDetails userDetails){
//        Room room = findRoomById(id);
//        if (checkRoomCreator(room, userDetails))
//            return ResponseEntity.badRequest()
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body("Вы не можете получить доступ к информации о чужой комнате");
//        List<ProductDto> productsDto = room.getProducts().stream().map(productService::getInfo).toList();
//        return ResponseEntity.ok(productsDto);
//
//    }

//    public ResponseEntity<?> getTotalPrice(Long id, UserDetails userDetails){
//        Room room = findRoomById(id);
//        if (checkRoomCreator(room, userDetails))
//            return ResponseEntity.badRequest()
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body("Вы не можете получить доступ к информации о чужой комнате");
//        return ResponseEntity.ok(room.getPrice());
//    }

    public ResponseEntity<?> getSingleRoomInfo(Long id, UserDetails userDetails) {
        Room room = findRoomById(id);
        return processRoomRequest(room, userDetails, () -> ResponseEntity.ok(getInfo(room.getInvitationLink())),
                "Вы не можете получить доступ к чужой комнате");
    }

    public ResponseEntity<?> getAllRoomParticipants(Long id, UserDetails userDetails) {
        Room room = findRoomById(id);
        List<UserDto> users = room.getUsers().stream().map(userService::getInfo).toList();
        return processRoomRequest(room, userDetails, () -> ResponseEntity.ok(users),
                "Вы не можете получить доступ к информации о чужой комнате");
    }

    public ResponseEntity<?> getAllRoomProducts(Long id, UserDetails userDetails) {
        Room room = findRoomById(id);
        List<ProductDto> productsDto = room.getProducts().stream().map(productService::getInfo).toList();
        return processRoomRequest(room, userDetails, () -> ResponseEntity.ok(productsDto),
                "Вы не можете получить доступ к информации о чужой комнате");
    }

    public ResponseEntity<?> getTotalPrice(Long id, UserDetails userDetails) {
        Room room = findRoomById(id);
        return processRoomRequest(room, userDetails, () -> ResponseEntity.ok(room.getPrice()),
                "Вы не можете получить доступ к информации о чужой комнате");
    }

    private ResponseEntity<?> processRoomRequest(Room room, UserDetails userDetails,
                                                 Supplier<ResponseEntity<?>> successResponseSupplier, String errorMessage) {
        if (checkRoomCreator(room, userDetails)) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorMessage);
        }
        return successResponseSupplier.get();
    }

    public String save(RoomCreationForm form, UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());

        Room room = new Room(form.name(), form.category());
        room.setName(form.name());
        room.setRoomCategory(form.category());
        room.setInvitationLink(linkService.createInvitationLink());
        room.setProducts(new ArrayList<>());
        room.setUsers(new ArrayList<>());
        room.setCreatedAt(new Timestamp(new Date().getTime()));
        room.setCreator(user);

        user.getCreatedRooms().add(room);
        roomRepository.save(room);
        return room.getInvitationLink();
    }

    public String update(Long id, RoomUpdateForm form, UserDetails userDetails) {
        Room room = findRoomById(id);
        if (checkRoomCreator(room, userDetails))
            return "У вас нет прав для изменения комнаты";
        if (form.name() != null)
            room.setName(form.name());

        if (form.description() != null)
            room.setDescription(form.description());

        if (form.category() != null)
            room.setRoomCategory(form.category());
        return "Комната изменена";
    }

    public String delete(Long id, UserDetails userDetails) {
        Room room = findRoomById(id);
        if (checkRoomCreator(room, userDetails))
            return "У вас нет прав для удаления комнаты";
        roomRepository.delete(room);
        return "Комната удалена";
    }
}
