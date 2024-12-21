package com.haole.bupthotelbackend.request;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String username;
    private String password;
    private String confirmPassword;
    private Integer gender;
}
