package com.ninadproject.Office_Management_App.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity

public class AttendanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private Long empId;
    @Enumerated(EnumType.STRING)
    private AttendanceStatus status; // P, H, A

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;


}