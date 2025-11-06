package com.ninadproject.Office_Management_App.dto;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String status; // TO_DO, IN_PROGRESS, DONE
    private String priority; // LOW, MEDIUM, HIGH

    @ManyToOne
    private Project project;

    @ManyToOne
    private Employee assignedTo;

    @ManyToOne
    private Employee createdBy;

}