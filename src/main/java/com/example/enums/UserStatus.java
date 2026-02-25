package com.example.enums;

import lombok.Getter;

@Getter
public enum UserStatus {

    ACTIVE(1),
    LOCKED(0),
    DELETED(-1);

    private final int code;

    UserStatus(int code) {
        this.code = code;
    }

}
