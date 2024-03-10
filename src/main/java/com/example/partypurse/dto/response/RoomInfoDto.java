package com.example.partypurse.dto.response;

import com.example.partypurse.models.ERoomCategory;


import java.sql.Timestamp;
import java.util.List;

public record RoomInfoDto(Long id, String name, UserInfoDto creator,
                          String invitationLink, String description, Timestamp createdAt,
                          ERoomCategory roomCategory, List<UserInfoDto> users,
                          List<ProductInfoDto> products){
}