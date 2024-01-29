package com.example.partypurse.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "room_data")
public class RoomData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "invitation_link", unique = true, nullable = false)
    private String invitationLink;

    private String description;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "room_category", nullable = false)
    private ERoomCategory roomCategory;

    @OneToOne
    @JoinColumn(name = "room_id")
    private Room room;

}