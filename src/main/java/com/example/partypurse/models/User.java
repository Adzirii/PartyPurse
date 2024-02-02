package com.example.partypurse.models;

import io.jsonwebtoken.security.Password;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

//TODO: Добавить поля для банковской карты

@Getter
@Setter
@Entity
@Table(name = "users")
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Room> createdRooms;

    @ManyToMany(mappedBy = "users", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Room> visitedRooms;

}