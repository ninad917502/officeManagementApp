package com.ninadproject.Office_Management_App.Service;

import com.ninadproject.Office_Management_App.Entity.Issue;
import com.ninadproject.Office_Management_App.dto.IssueResponseDTO;

import java.security.Principal;
import java.util.List;

public interface IssueService {
    Issue createIssue(Issue issue, Principal principal);
    List<Issue> getIssuesByProject(Long projectId);
    Issue updateStatus(Long issueId, String newStatus);
    List<Issue> getIssuesByUser(Long employeeId);
    List<Issue> getIssuesByUserAndProject(Long userId, Long projectId);
    List<Issue> getIssuesByStatus(String status);
    IssueResponseDTO getIssueDetailsById(Long id);
    List<Issue> getIssuesByEmployeeId(Long employeeId);

}

