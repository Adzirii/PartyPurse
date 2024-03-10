package com.example.partypurse.dto.request;

import com.example.partypurse.models.ERoomCategory;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomUpdateForm {
    @Size(min = 5, max = 50, message = "Room name must be between 5 and 30 symbols")
    private String name;
    @Size(min = 5, max = 50, message = "Room description must be between 5 and 30 symbols")
    private String description;
    private ERoomCategory category;
}
