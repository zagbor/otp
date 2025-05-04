package com.zagbor.otp.controller.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String login;
    private String password;
    private String role; // "USER" или "ADMIN"
    private String email;
    private String phoneNumber;
    private String telegram;
}
