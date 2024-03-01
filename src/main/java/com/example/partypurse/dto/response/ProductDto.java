package com.example.partypurse.dto.response;

import com.example.partypurse.models.User;

import java.io.Serializable;

/**
 * DTO for {@link com.example.partypurse.models.Product}
 */
public record ProductDto(Long id, String productName, String category, Double price, Long userId) implements Serializable {
}