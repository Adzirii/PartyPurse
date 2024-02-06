package com.example.partypurse.services;

import com.example.partypurse.dto.request.RoomCreationForm;
import com.example.partypurse.models.Room;
import com.example.partypurse.models.User;
import com.example.partypurse.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserService userService;

    public Optional<Room> findRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public void save(RoomCreationForm form, UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername()).get();

        Room room = new Room(form.name(), form.category());
        room.setName(form.name());
        room.setRoomCategory(form.category());
        room.setInvitationLink("O4ko");
        room.setProducts(new ArrayList<>());
        room.setUsers(new ArrayList<>());
        room.setCreatedAt(new Timestamp(new Date().getTime()));
        room.setCreator(user);

        user.getCreatedRooms().add(room);

        roomRepository.save(room); // Создается id

        room.setInvitationLink(String.format("ссылка на комнату № %s", room.getId())); // используем id
         roomRepository.save(room);
    }
}
