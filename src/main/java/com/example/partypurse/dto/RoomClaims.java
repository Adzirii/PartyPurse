package com.example.partypurse.dto;

import com.example.partypurse.models.ERoomCategory;
import com.example.partypurse.models.User;

public record RoomClaims(Long id, String name, ERoomCategory category, User creator) {
}
