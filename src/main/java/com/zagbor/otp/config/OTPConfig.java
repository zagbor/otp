package com.zagbor.otp.config;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "otp_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OTPConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "otp_length", nullable = false)
    private Integer otpLength;

}
