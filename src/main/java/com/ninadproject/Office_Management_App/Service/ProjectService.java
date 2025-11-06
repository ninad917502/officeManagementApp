package com.ninadproject.Office_Management_App.Service;

import com.ninadproject.Office_Management_App.Entity.Employee;
import com.ninadproject.Office_Management_App.Entity.Project;
import com.ninadproject.Office_Management_App.dto.AssignmentRequest;

import java.security.Principal;
import java.util.List;

public interface ProjectService {
    Project create(Project project, Principal principal);
    List<Project> getAllCreatedByUser(Principal principal);
    String assignEmployee(Long projectId, AssignmentRequest request);
    List<Project> getProjectsAssignedTo(Employee employee);
    List<String> getProjectsAssignedTo(Long employeeId);
}
