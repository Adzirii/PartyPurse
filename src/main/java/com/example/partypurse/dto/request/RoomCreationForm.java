package com.example.partypurse.dto.request;

import com.example.partypurse.models.ERoomCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoomCreationForm(
        @NotBlank
        @Size(max = 30)
        String name,
        @Size(max = 30)
        String description,
        @NotBlank
        ERoomCategory category) {
}
