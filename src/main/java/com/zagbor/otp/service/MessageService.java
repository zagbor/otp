package com.zagbor.otp.service;

import com.zagbor.otp.entity.User;

public class MessageService {
    private final MessageSender sender;

    public MessageService(MessageSender sender) {
        this.sender = sender;
    }

    public void sendMessage(User user, String code) {
        sender.send(user, code);
    }
}