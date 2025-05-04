package com.zagbor.otp.controller;

import com.zagbor.otp.controller.dto.OTPRequest;
import com.zagbor.otp.controller.dto.OTPValidationRequest;
import com.zagbor.otp.service.OTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/otp")
@RequiredArgsConstructor
@Slf4j
public class OTPController {

    private final OTPService otpService;

    @PostMapping("/generate")
    public String generateOtp(@RequestBody OTPRequest request) {
        log.debug("Получен POST-запрос на генерацию OTP: operationId={}",
                request.getOperationId());
        String otp = otpService.generateOtp(request);
        log.info("OTP сгенерирован для operationId={}", request.getOperationId());
        return otp;
    }

    @PostMapping("/validate")
    public boolean validateOtp(@RequestBody OTPValidationRequest request) {
        log.debug("Получен POST-запрос на валидацию OTP: operationId={}",
                request.getOperationId());
        boolean isValid = otpService.validateOtp(request);
        if (isValid) {
            log.info("OTP успешно подтверждён: operationId={}", request.getOperationId());
        } else {
            log.warn("OTP не прошёл проверку: operationId={}", request.getOperationId());
        }
        return isValid;
    }
}
