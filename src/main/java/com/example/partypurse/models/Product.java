package com.example.partypurse.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToOne
    private User adder;

    //Hello como estas
}