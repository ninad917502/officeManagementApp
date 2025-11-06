package com.ninadproject.Office_Management_App.Entity;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;

}