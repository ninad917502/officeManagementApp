package com.ninadproject.Office_Management_App.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class ProjectAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employeeId")
    @JsonBackReference("assignments")
    private Employee employee;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "project_id")
    
    private Project project;

    private LocalDate startDate;
    private LocalDate endDate;  // nullable if ongoing


}