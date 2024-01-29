package com.example.partypurse.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_data")
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;



//    @Column
//    private String cardNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}