package com.example.partypurse.dto.request;

import com.example.partypurse.models.ERoomCategory;

public record RoomUpdateForm(String name, String description, ERoomCategory category) {
}
