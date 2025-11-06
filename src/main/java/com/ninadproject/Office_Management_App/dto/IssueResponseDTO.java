package com.ninadproject.Office_Management_App.dto;

import lombok.Data;

@Data
public class IssueResponseDTO {

    private long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String projectName;

    public IssueResponseDTO(Long id, String title, String description, String status, String priority, String projectName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.projectName = projectName;
    }
}
