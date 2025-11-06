package com.ninadproject.Office_Management_App.Config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


public class AppConfig {
  
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}