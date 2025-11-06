package com.ninadproject.Office_Management_App.ServiceImpl;

import com.ninadproject.Office_Management_App.Entity.OtpVerification;
import com.ninadproject.Office_Management_App.Repository.OtpVerificationRepository;
import com.ninadproject.Office_Management_App.Service.OtpService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpVerificationRepository otpRepository;
    private final JavaMailSender mailSender;

    @Override
    @Transactional
    public String generateAndSendOtp(String email) {
        try {
            String otp = String.format("%06d", new Random().nextInt(999999));

            OtpVerification otpVerification = OtpVerification.builder()
                    .email(email)
                    .otp(otp)
                    .expirationTime(LocalDateTime.now().plusMinutes(5))
                    .used(false)
                    .build();

            otpRepository.save(otpVerification);

            // send mail
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Your OTP Code");
            message.setText("Your OTP code is: " + otp + " (valid for 5 minutes).");
            mailSender.send(message);

            log.info("OTP sent successfully to {}", email);
            return "OTP sent successfully to " + email;

        } catch (Exception ex) {
            log.error("Failed to send OTP to {}: {}", email, ex.getMessage(), ex);
            throw new RuntimeException("Failed to send OTP. Please try again later.", ex);
        }
    }

    @Override
    @Transactional
    public String verifyOtp(String email, String otp) {
        OtpVerification otpRecord = otpRepository.findByEmailAndOtp(email, otp)
                .orElseThrow(() -> {
                    log.error("Invalid OTP entered for email: {}", email);
                    return new RuntimeException("Invalid OTP.");
                });

        if (otpRecord.isUsed()) {
            log.warn("OTP already used for email: {}", email);
            throw new RuntimeException("OTP already used.");
        }

        if (otpRecord.getExpirationTime().isBefore(LocalDateTime.now())) {
            log.warn("Expired OTP for email: {}", email);
            throw new RuntimeException("OTP expired.");
        }

        otpRecord.setUsed(true);
        otpRepository.save(otpRecord);

        log.info("OTP verified successfully for email: {}", email);
        return "OTP verified successfully";
    }
}
