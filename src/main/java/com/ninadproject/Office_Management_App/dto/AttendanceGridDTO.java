package com.ninadproject.Office_Management_App.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class AttendanceGridDTO {
    private Long employeeId;
    private String employeeName;
    
    private Map<String, String> attendance;

    private int presentCount;
    private int absentCount;
    private int holidayCount;
    
    public AttendanceGridDTO() {}

    public AttendanceGridDTO(Long id, String name, Map<String, String> att,
                             int p, int a, int h) {
        this.employeeId = id;
        this.employeeName = name;
       
        this.attendance = att;
        this.presentCount = p;
        this.absentCount = a;
        this.holidayCount = h;
    }


}