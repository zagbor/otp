package com.zagbor.otp.controller.dto;

import lombok.Data;

@Data
public class AuthRequest {

    private String login;
    private String password;
}
