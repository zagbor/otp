package com.zagbor.otp.service;

import com.zagbor.otp.controller.dto.OTPRequest;
import com.zagbor.otp.controller.dto.OTPValidationRequest;

public interface OTPService {


   String generateOtp(OTPRequest request);

   boolean validateOtp(OTPValidationRequest request);
}