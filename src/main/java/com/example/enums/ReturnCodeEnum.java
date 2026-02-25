package com.example.enums;

import lombok.Getter;
@Getter
public enum ReturnCodeEnum {

    SUCCESS(200, "Success"),
    UNAUTHORIZED(401, "Authentication Deny"),
    FORBIDDEN(403, "Authorization Deny"),
    NOT_FOUND(404, "Not Found"),
    ERROR(500, "Internal Error");

    private final int code;
    private String msg;

    ReturnCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ReturnCodeEnum setMsg(String msg) {
        this.msg = msg;
        return this;
    }


}
