package com.example.partypurse.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserUpdateDto {

    @Size(min = 5, max = 30, message = "Username must be between 5 and 30 symbols")
    String username;

    @Size(min = 5, max = 30, message = "Firstname must be between 5 and 30 symbols")
    String firstName;

    @Size(min = 5, max = 30, message = "Lastname must be between 5 and 30 symbols")
    String lastName;

}
