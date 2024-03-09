package com.example.partypurse.services;

import com.example.partypurse.dto.request.ProductCreationForm;
import com.example.partypurse.dto.request.RoomCreationForm;
import com.example.partypurse.dto.request.RoomUpdateForm;
import com.example.partypurse.dto.response.ProductDto;
import com.example.partypurse.dto.response.RoomDto;
import com.example.partypurse.dto.response.UserInfoDto;
import com.example.partypurse.models.Product;
import com.example.partypurse.models.Room;
import com.example.partypurse.models.User;
import com.example.partypurse.repositories.RoomRepository;
import com.example.partypurse.repositories.UserRepository;
import com.example.partypurse.util.errors.UserAuthorityException;
import lombok.RequiredArgsConstructor;
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

    public void addProduct(Long id, ProductCreationForm productCreationForm, CustomUserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        var product = productService.save(productCreationForm, user);
        Room room = findRoomById(id);
        if(!(room.getUsers().contains(user) || room.getCreator().equals(user)))
            throw new RuntimeException("Вы не являетесь участником данной комнаты");
        room.getProducts().add(product);
        roomRepository.save(room);

    }

    public void deleteProduct(Long roomId, Long productId, CustomUserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Product product = productService.getById(productId);
        Room room = findRoomById(roomId);
        if(!(product.getAdder().equals(user) || room.getCreator().equals(user)))
            throw new RuntimeException("Вы не можете удалить данный продукт");
        room.getProducts().remove(product);
        roomRepository.save(room);
        productService.getById(productId);
    }

    public Double getAllProductsCost(Long roomId){
        Room room = findRoomById(roomId);
        var sum = 0d;
        for (var product:room.getProducts()){
            sum+=product.getPrice();
        }
        return sum;
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
        checkRoomCreator(room, userDetails);
        room.getUsers().remove(user);
        user.getVisitedRooms().remove(room);
        roomRepository.save(room);
        userRepository.save(user);
        return "Пользователь исключен";
    }

    public String getInviteLink(Long id, UserDetails userDetails) {
        Room room = findRoomById(id);
        checkRoomCreator(room, userDetails);
        return room.getInvitationLink();
    }
    public RoomDto getInfoById(Long id) {
        Room room = findRoomById(id);
        List<UserInfoDto> users = room.getUsers().stream().map(userService::getInfo).toList();
        List<ProductDto> products = room.getProducts().stream().map(productService::getInfo).toList();
        return new RoomDto(room.getId(), room.getName(), userService.getInfo(room.getCreator()), room.getInvitationLink(), room.getDescription(),
                room.getCreatedAt(), room.getRoomCategory(), users, products);
    }

    public RoomDto getInfoByLink(String link) {
        Room room = findByLink(link);
        List<UserInfoDto> users = room.getUsers().stream().map(userService::getInfo).toList();
        List<ProductDto> products = room.getProducts().stream().map(productService::getInfo).toList();
        return new RoomDto(room.getId(), room.getName(), userService.getInfo(room.getCreator()), room.getInvitationLink(), room.getDescription(),
                room.getCreatedAt(), room.getRoomCategory(), users, products);
    }

    public List<RoomDto> getAllUserRooms(UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        return user.getCreatedRooms().stream().map(room -> getInfoByLink(room.getInvitationLink())).toList();
    }

    private void checkRoomCreator(Room room, UserDetails userDetails) throws UserAuthorityException {

        User user = userService.findByUsername(userDetails.getUsername());
        if (!room.getCreator().getId().equals(user.getId()))
            throw new UserAuthorityException("У вас не достаточно прав для этого действия.");
    }

    public ResponseEntity<?> getSingleRoomInfo(Long id, UserDetails userDetails) {
        Room room = findRoomById(id);
        return processRoomRequest(room, userDetails, () -> ResponseEntity.ok(getInfoByLink(room.getInvitationLink())));
    }

    public ResponseEntity<?> getAllRoomParticipants(Long id, UserDetails userDetails) {
        Room room = findRoomById(id);
        List<UserInfoDto> users = room.getUsers().stream().map(userService::getInfo).toList();
        return processRoomRequest(room, userDetails, () -> ResponseEntity.ok(users));
    }

    public ResponseEntity<?> getAllRoomProducts(Long id, UserDetails userDetails) {
        Room room = findRoomById(id);
        List<ProductDto> productsDto = room.getProducts().stream().map(productService::getInfo).toList();
        return processRoomRequest(room, userDetails, () -> ResponseEntity.ok(productsDto));
    }

    public ResponseEntity<?> getTotalPrice(Long id, UserDetails userDetails) {
        Room room = findRoomById(id);
        return processRoomRequest(room, userDetails, () -> ResponseEntity.ok(getAllProductsCost(id)));
    }

    private ResponseEntity<?> processRoomRequest(Room room, UserDetails userDetails,
                                                 Supplier<ResponseEntity<?>> successResponseSupplier) {
        //checkRoomCreator(room, userDetails);
        return successResponseSupplier.get();
    }

    public String save(RoomCreationForm form, UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());

        Room room = new Room();
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
        checkRoomCreator(room, userDetails);

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
        checkRoomCreator(room, userDetails);
        roomRepository.delete(room);
        return "Комната удалена";
    }
}
