package com.zagbor.otp.service;

import com.zagbor.otp.entity.User;

public interface MessageSender {
    void send(User user, String code);
}