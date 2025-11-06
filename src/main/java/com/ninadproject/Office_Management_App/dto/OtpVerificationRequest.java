package com.ninadproject.Office_Management_App.dto;

import lombok.Data;


@Data
public class OtpVerificationRequest {

    private String email;
    private String otp;
}
