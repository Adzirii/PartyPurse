package com.example.partypurse.dto.request;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductUpdateForm(

        @Size(max = 30)
        String name,

        @Size(max = 30)
        String category,
        @DecimalMin("0")
        @DecimalMax("999999")
        Double price) {
}
