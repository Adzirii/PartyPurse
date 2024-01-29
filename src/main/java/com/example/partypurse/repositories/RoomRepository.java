package com.example.partypurse.repositories;

import com.example.partypurse.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}