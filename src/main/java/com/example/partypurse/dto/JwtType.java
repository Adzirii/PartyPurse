package com.example.partypurse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtType {
    ACCESS(1000 * 60 * 30),     // 30 минут
    REFRESH(1000 * 60 * 60 * 2), // 2 часа
    ROOM(1000 * 60 * 60 * 168); // неделя

    private final int expireTime;

    public static int getExpireTime(JwtType type) {
        if(type.equals(ACCESS)) {
            return ACCESS.getExpireTime();
        }
        return REFRESH.getExpireTime();
    }
}
