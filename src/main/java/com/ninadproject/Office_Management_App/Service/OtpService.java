package com.ninadproject.Office_Management_App.Service;

public interface OtpService {

    /**
     * Generate and send OTP to email.
     * @param email recipient email
     * @return success message
     */
    String generateAndSendOtp(String email);

    /**
     * Verify OTP for a given email.
     * @param email user email
     * @param otp provided otp
     * @return verification message
     */
    String verifyOtp(String email, String otp);
}
