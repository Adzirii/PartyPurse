package com.example.partypurse.dto.request;

import com.example.partypurse.models.ERoomCategory;

public record RoomCreationForm(String name, ERoomCategory category) {
}
