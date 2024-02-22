package com.example.partypurse.dto.request;

import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    String username;
    String firstName;
    String lastName;

}
