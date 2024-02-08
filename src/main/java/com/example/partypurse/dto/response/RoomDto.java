package com.example.partypurse.dto.response;

import com.example.partypurse.models.ERoomCategory;
import com.example.partypurse.models.User;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * DTO for {@link com.example.partypurse.models.Room}
 */
public record RoomDto(Long id, String name, UserDto creator, String invitationLink, String description, Timestamp createdAt,
                      ERoomCategory roomCategory, List<UserDto> users,
                      List<ProductDto> products) implements Serializable {
}