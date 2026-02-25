package com.example.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserVO {
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
}
