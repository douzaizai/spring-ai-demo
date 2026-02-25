package com.example.response;

import com.example.vo.UserVO;
import lombok.Data;

@Data
public class LoginResponse {
    private UserVO user;
    private String token;
}
