package com.zagbor.otp.repository;

import com.zagbor.otp.entity.OTPCode;
import com.zagbor.otp.entity.OTPState;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPCodeRepository extends JpaRepository<OTPCode, Long> {
    Optional<OTPCode> findByCode(String code);
    Optional<OTPCode> findByOperationId(String operationId);
    List<OTPCode> findByCreatedAtBeforeAndState(LocalDateTime dateTime, OTPState state);
}
