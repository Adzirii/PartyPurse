package com.example.partypurse.dto.request;

import com.example.partypurse.models.User;

public record ProductCreationForm(String name, String category, Double price) {
}
