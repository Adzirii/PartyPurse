package com.example.partypurse.services;

import com.example.partypurse.dto.request.RoomCreationForm;
import com.example.partypurse.models.Room;
import com.example.partypurse.models.User;
import com.example.partypurse.repositories.RoomRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserService userService;

    public Optional<Room> findRoomById(Long id){
        return roomRepository.findById(id);
    }

    public Room save(RoomCreationForm form, UserDetails userDetails){
        User user = userService.findByUsername(userDetails.getUsername()).get();
        Room room = new Room();
        room.setName(form.name());
        room.setRoomCategory(form.category());
        room.setCreatedAt(new Timestamp(new Date().getTime()));
        room.setCreator(user);
        user.getCreatedRooms().add(room);
        return roomRepository.save(room);
    }
}