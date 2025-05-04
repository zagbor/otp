package com.zagbor.otp.controller.dto;

import com.zagbor.otp.DeliveryType;
import lombok.Data;

@Data
public class OTPRequest {
    private String operationId;
    private DeliveryType deliveryType;
}
