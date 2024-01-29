package com.example.partypurse.models;

import io.jsonwebtoken.security.Password;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//TODO: Добавить поля для банковской карты

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Transient
    @Column(nullable = false)
    private String passwordConfirm;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserData userData;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Room> createdRooms;

    @ManyToMany(mappedBy = "users", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Room> visitedRooms;

}