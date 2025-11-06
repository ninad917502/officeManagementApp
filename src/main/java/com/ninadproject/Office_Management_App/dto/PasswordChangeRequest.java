package com.ninadproject.Office_Management_App.dto;

import lombok.Data;

@Data
public class PasswordChangeRequest {

    private String username;
    private String newPassword;

}
