package com.ninadproject.Office_Management_App.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpVerification {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)

    private  Long employeeId;
    private  String email;
    private String otp;
    private LocalDateTime expirationTime;
    private boolean used;


}
