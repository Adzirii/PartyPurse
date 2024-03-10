package com.example.partypurse.dto.response;

import java.io.Serializable;

/**
 * DTO for {@link com.example.partypurse.models.Product}
 */
public record ProductInfoDto(Long id, String productName, String category, Double price, Long userId) implements Serializable {
}