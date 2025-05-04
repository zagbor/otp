package com.zagbor.otp.service.impl;


import com.zagbor.otp.DeliveryType;
import com.zagbor.otp.config.OTPConfig;
import com.zagbor.otp.controller.dto.OTPRequest;
import com.zagbor.otp.controller.dto.OTPValidationRequest;
import com.zagbor.otp.entity.OTPCode;
import com.zagbor.otp.entity.OTPState;
import com.zagbor.otp.entity.User;
import com.zagbor.otp.repository.OTPCodeRepository;
import com.zagbor.otp.repository.OTPConfigRepository;
import com.zagbor.otp.service.MessageService;
import com.zagbor.otp.service.OTPService;
import com.zagbor.otp.service.UserService;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {

    private final OTPCodeRepository otpCodeRepository;
    private final OTPConfigRepository otpConfigRepository;
    private final SmsSender smsSender;
    private final UserService userService;
    private final TelegramSender telegramSender;
    private final EmailSender emailSender;

    private static final SecureRandom random = new SecureRandom();

    public String generateOtp(OTPRequest request) {
        OTPConfig config = otpConfigRepository.findTopByOrderByIdAsc();
        int length = config != null ? config.getOtpLength() : 6;
        String code = generateNumericCode(length);

        User currentUser = getCurrentUser();

        OTPCode otp = OTPCode.builder()
                .code(code)
                .operationId(request.getOperationId())
                .createdAt(LocalDateTime.now())
                .user(currentUser)
                .state(OTPState.ACTIVE)
                .build();

        otpCodeRepository.save(otp);

        switch (request.getDeliveryType()) {
            case FILE -> saveToFile(currentUser.getLogin(), code);
            case EMAIL -> {
                var messageService = new MessageService(emailSender);
                messageService.sendMessage(currentUser, code);
            }
            case SMS -> {
                var messageService = new MessageService(smsSender);
                messageService.sendMessage(currentUser, code);
            }
            case TELEGRAM -> {
                var messageService = new MessageService(telegramSender);
                messageService.sendMessage(currentUser, code);
            }
            default -> throw new RuntimeException("Неподдерживаемый тип доставки");
        }

        return code;
    }

    public boolean validateOtp(OTPValidationRequest request) {
        Optional<OTPCode> otpOptional = otpCodeRepository.findByOperationId(request.getOperationId());
        if (otpOptional.isEmpty()) {
            return false;
        }

        if (otpOptional.get().getState() != OTPState.ACTIVE) {
            return false;
        }

        OTPCode otp = otpOptional.get();
        otp.setState(OTPState.USED);
        otpCodeRepository.save(otp);
        return otp.getCode().equals(request.getCode());
    }

    private String generateNumericCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private void saveToFile(String username, String code) {
        try (FileWriter writer = new FileWriter("otp_" + username + ".txt")) {
            writer.write("OTP код: " + code);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи OTP в файл");
        }
    }

    private User getCurrentUser() {
        String login = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userService.getByLogin(login);
    }
}