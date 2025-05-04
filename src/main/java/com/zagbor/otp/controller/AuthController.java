package com.zagbor.otp.controller;

import com.zagbor.otp.controller.dto.AuthRequest;
import com.zagbor.otp.controller.dto.AuthResponse;
import com.zagbor.otp.controller.dto.RegisterRequest;
import com.zagbor.otp.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        log.debug("Получен POST-запрос на регистрацию пользователя: email={}, username={}",
                request.getEmail(), request.getLogin());
        AuthResponse response = authService.register(request);
        log.info("Пользователь успешно зарегистрирован");
        return response;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        log.debug("Получен POST-запрос на вход: login={}", request.getLogin());
        AuthResponse response = authService.authenticate(request);
        log.info("Пользователь успешно вошёл");
        return response;
    }
}
