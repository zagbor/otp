package com.zagbor.otp.service.impl;

import com.zagbor.otp.entity.User;
import com.zagbor.otp.service.MessageSender;
import jakarta.annotation.PostConstruct;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailSender implements MessageSender {
    @Value("${mail.smtp.host}")
    private String host;

    @Value("${mail.smtp.port}")
    private String port;

    @Value("${mail.smtp.auth}")
    private String auth;

    @Value("${mail.smtp.starttls.enable}")
    private String starttls;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    private Session session;


    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }


    @Override
    public void send(User user, String code) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmail())
            );
            message.setSubject("Твой OTP");
            message.setText(code);

            Transport.send(message);
            System.out.println("Письмо успешно отправлено.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}