package com.ninadproject.Office_Management_App.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class AttendanceRecordDTO {
    private Long empId;
    private LocalDate date;

    private AttendanceStatus status;

    
}