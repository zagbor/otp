package com.zagbor.otp.controller;

import com.zagbor.otp.config.OTPConfig;
import com.zagbor.otp.entity.User;
import com.zagbor.otp.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @PutMapping("/otp-config")
    public OTPConfig updateConfig(@RequestBody OTPConfig config) {
        log.debug("Получен PUT-запрос на обновление конфигурации OTP: {}", config);
        OTPConfig updatedConfig = adminService.updateOtpConfig(config);
        log.info("Конфигурация OTP обновлена успешно");
        return updatedConfig;
    }

    @GetMapping("/users")
    public List<User> getAllNotAdminUsers() {
        log.debug("Получен GET-запрос на получение всех не-админ пользователей");
        List<User> users = adminService.getAllNonAdminUsers();
        log.info("Получено {} не-админ пользователей", users.size());
        return users;
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.debug("Получен DELETE-запрос на удаление пользователя с ID: {}", id);
        adminService.deleteUserAndOtps(id);
        log.warn("Пользователь с ID {} и его OTP коды были удалены", id);
    }
}
