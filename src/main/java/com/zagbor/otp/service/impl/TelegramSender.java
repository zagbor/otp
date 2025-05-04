package com.zagbor.otp.service.impl;

import com.zagbor.otp.entity.User;
import com.zagbor.otp.service.MessageSender;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TelegramSender implements MessageSender {

    @Value("${telegram.token}")
    private String token;

    @Override
    public void send(User user, String code) {
        String message = String.format("Ваш код подтверждения: %s", code);
        String encodedMessage = urlEncode(message);

        String url = String.format(
                "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
                token,
                user.getTelegramCode(),
                encodedMessage
        );

        sendTelegramRequest(url);
    }

    private void sendTelegramRequest(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}