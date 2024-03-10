package com.example.partypurse.dto.request;


import jakarta.validation.constraints.*;

public record ProductCreationForm(
        @NotBlank
        @Size(max = 30)
        String name,
        @NotBlank
        @Size(max = 30)
        String category,

        @NotNull
        @DecimalMin("0")
        @DecimalMax("999999")
        Double price) {
}
