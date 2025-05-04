package com.zagbor.otp.service;

import com.zagbor.otp.config.OTPConfig;
import com.zagbor.otp.entity.OTPCode;
import com.zagbor.otp.entity.User;
import com.zagbor.otp.repository.OTPCodeRepository;
import com.zagbor.otp.repository.OTPConfigRepository;
import com.zagbor.otp.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final OTPConfigRepository otpConfigRepository;
    private final UserRepository userRepository;
    private final OTPCodeRepository otpCodeRepository;

    public OTPConfig updateOtpConfig(OTPConfig config) {
        otpConfigRepository.deleteAll();
        return otpConfigRepository.save(config);
    }

    public List<User> getAllNonAdminUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> !user.getRole().equalsIgnoreCase("ADMIN"))
                .toList();
    }

    public void deleteUserAndOtps(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        List<OTPCode> codes = otpCodeRepository.findAll()
                .stream()
                .filter(code -> code.getUser().getId().equals(userId))
                .toList();

        otpCodeRepository.deleteAll(codes);
        userRepository.delete(user);
    }
}
