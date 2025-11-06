package com.ninadproject.Office_Management_App.Controller;

import com.ninadproject.Office_Management_App.Service.OtpService;
import com.ninadproject.Office_Management_App.dto.OtpRequest;
import com.ninadproject.Office_Management_App.dto.OtpVerificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    // Send OTP (JSON: { "email": "test@gmail.com" })
    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(otpService.generateAndSendOtp(request.getEmail()));
    }

    // Verify OTP (JSON: { "email": "test@gmail.com", "otp": "123456" })
    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest request) {
        return ResponseEntity.ok(otpService.verifyOtp(request.getEmail(), request.getOtp()));
    }
}
