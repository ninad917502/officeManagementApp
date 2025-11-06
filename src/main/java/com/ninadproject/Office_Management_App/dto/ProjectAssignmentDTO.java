package com.ninadproject.Office_Management_App.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectAssignmentDTO {
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;

    public ProjectAssignmentDTO(String projectName, LocalDate startDate, LocalDate endDate) {
        this.projectName = projectName;
        this.startDate = startDate;
        this.endDate = endDate;
    }


}