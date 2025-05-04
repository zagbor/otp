package com.zagbor.otp.repository;

import com.zagbor.otp.config.OTPConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPConfigRepository extends JpaRepository<OTPConfig, Long> {
    OTPConfig findTopByOrderByIdAsc();
}

