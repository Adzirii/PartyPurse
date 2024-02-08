package com.example.partypurse.dto.response;

import com.example.partypurse.models.EProductCategory;

import java.io.Serializable;

/**
 * DTO for {@link com.example.partypurse.models.Product}
 */
public record ProductDto(Long id, String productName, EProductCategory category, Double price) implements Serializable {
}