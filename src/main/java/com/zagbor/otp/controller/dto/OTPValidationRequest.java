package com.zagbor.otp.controller.dto;

import lombok.Data;

@Data
public class OTPValidationRequest {
    private String operationId;
    private String code;
}
