package com.example.partypurse.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "rooms")
@RequiredArgsConstructor
public class Room {
    @Id
//    @SequenceGenerator( name = "room_seq", sequenceName = "room_sequence", allocationSize = 1, initialValue = 20 )
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JsonBackReference
    private User creator;

    @Column(name = "invitation_link", unique = true)
    private String invitationLink;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "room_category", nullable = false)
    private ERoomCategory roomCategory;

    @Column(name = "total_price")
    private Double price;


    @ManyToMany
    @JoinTable(
            name = "room_user",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    @ManyToMany
    @JoinTable(
            name = "room_product",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    public Room(String name, ERoomCategory category) {
        setName(name);
        setRoomCategory(category);
        setProducts(new ArrayList<>());
        setUsers(new ArrayList<>());
    }
}