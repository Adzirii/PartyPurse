package com.example.partypurse.services;

import com.example.partypurse.dto.request.RoomCreationForm;
import com.example.partypurse.dto.response.ProductDto;
import com.example.partypurse.dto.response.RoleDto;
import com.example.partypurse.dto.response.RoomDto;
import com.example.partypurse.dto.response.UserDto;
import com.example.partypurse.models.Product;
import com.example.partypurse.models.Room;
import com.example.partypurse.models.User;
import com.example.partypurse.repositories.RoomRepository;
import com.example.partypurse.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserService userService;
    private final ProductService productService;
    private final LinkService linkService;

    public Room findRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Такой комнаты не существует"));
    }

    public Room findByLink(String link){
        return roomRepository.findByInvitationLink(link).orElseThrow(() -> new IllegalArgumentException("Комнаты с такой ссылкой не существует"));
    }

    public String join(String inviteLink, CustomUserDetails userDetails){
        User user = userService.findByUsername(userDetails.getUsername());
        Room room = findByLink(inviteLink);

        if (room.getUsers().contains(user))
            return "Вы уже являетесь участником";
        room.getUsers().add(user);
        return "Вы успешно вошли";
    }

    public String getInviteLink(Long id, CustomUserDetails userDetails){
        User user = userService.findByUsername(userDetails.getUsername());
        Room room = findRoomById(id);

        if (!Objects.equals(room.getCreator().getId(), user.getId()))
            return "Только создатель комнаты может получить ссылку-приглашение";
        return room.getInvitationLink();
    }

    public RoomDto getInfo(String link){
        Room room = findByLink(link);
        List<UserDto> users = room.getUsers().stream().map(userService::getInfo).toList();
        List<ProductDto> products = room.getProducts().stream().map(productService::getInfo).toList();
        return new RoomDto(room.getId(), room.getName(), userService.getInfo(room.getCreator()), room.getInvitationLink(), room.getDescription(),
                room.getCreatedAt(), room.getRoomCategory(), users, products);
    }

    public List<RoomDto> getAllUserRooms(CustomUserDetails userDetails){
        User user = userService.findByUsername(userDetails.getUsername());

        return user.getCreatedRooms().stream().map(room -> getInfo(room.getInvitationLink())).toList();
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
}
