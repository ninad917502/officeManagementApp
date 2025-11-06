package com.ninadproject.Office_Management_App.ServiceImpl;

import com.ninadproject.Office_Management_App.Entity.Employee;
import com.ninadproject.Office_Management_App.Entity.Issue;
import com.ninadproject.Office_Management_App.Entity.Project;
import com.ninadproject.Office_Management_App.Repository.EmployeeRepository;
import com.ninadproject.Office_Management_App.Repository.IssueRepository;
import com.ninadproject.Office_Management_App.Repository.ProjectRepository;
import com.ninadproject.Office_Management_App.Service.IssueService;
import com.ninadproject.Office_Management_App.dto.IssueResponseDTO;
import com.ninadproject.Office_Management_App.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
public class IssueServiceImpl implements IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private EmployeeRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Issue createIssue(Issue issue, Principal principal) {
        try {
            log.info("Creating issue for user: {}", principal.getName());

            Employee employee = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + principal.getName()));

            Project project = projectRepository.findById(issue.getProject().getProject_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + issue.getProject().getProject_id()));

            issue.setCreatedBy(employee);
            issue.setProject(project);

            Issue saved = issueRepository.save(issue);
            log.info("Issue created with ID: {}", saved.getId());
            return saved;

        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("Unexpected error while creating issue", e);
            throw new RuntimeException("Failed to create issue", e);
        }
    }

    @Override
    public List<Issue> getIssuesByProject(Long projectId) {
        try {
            log.info("Fetching issues for project ID: {}", projectId);

            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

            List<Issue> issues = issueRepository.findByProject(project);
            if (issues.isEmpty()) {
                throw new ResourceNotFoundException("No issues found for project ID: " + projectId);
            }

            return issues;

        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("Unexpected error while fetching issues for project ID: {}", projectId, e);
            throw new RuntimeException("Failed to fetch issues for project", e);
        }
    }

    @Override
    public Issue updateStatus(Long issueId, String newStatus) {
        try {
            log.info("Updating issue status. ID: {}, New Status: {}", issueId, newStatus);

            Issue issue = issueRepository.findById(issueId)
                    .orElseThrow(() -> new ResourceNotFoundException("Issue not found with ID: " + issueId));

            issue.setStatus(newStatus);
            return issueRepository.save(issue);

        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("Unexpected error while updating status for issue ID: {}", issueId, e);
            throw new RuntimeException("Failed to update issue status", e);
        }
    }

    @Override
    public List<Issue> getIssuesByUser(Long employeeId) {
        try {
            log.info("Fetching issues for employee ID: {}", employeeId);

            List<Issue> issues = issueRepository.findByAssignedToEmployeeId(employeeId);
            if (issues.isEmpty()) {
                throw new ResourceNotFoundException("No issues found for employee ID: " + employeeId);
            }

            return issues;

        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("Unexpected error while fetching issues for employee ID: {}", employeeId, e);
            throw new RuntimeException("Failed to fetch issues for employee", e);
        }
    }

    @Override
    public List<Issue> getIssuesByUserAndProject(Long userId, Long projectId) {

        try {
            log.info("Fetching issues for user ID: {} and project ID: {}", userId, projectId);

            List<Issue> issues = issueRepository.findByUserIdAndProjectId(userId, projectId);
            if (issues.isEmpty()) {
                throw new ResourceNotFoundException("No issues found for user ID: " + userId + " and project ID: " + projectId);
            }

            return issues;

        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("Unexpected error while fetching issues for user {} and project {}", userId, projectId, e);
            throw new RuntimeException("Failed to fetch issues by user and project", e);
        }
    }

    @Override
    public List<Issue> getIssuesByStatus(String status) {
        try {
            log.info("Fetching issues with status: {}", status);

            List<Issue> issues = issueRepository.findByStatus(status.toUpperCase());
            if (issues.isEmpty()) {
                throw new ResourceNotFoundException("No issues found with status: " + status);
            }

            return issues;

        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("Unexpected error while fetching issues by status: {}", status, e);
            throw new RuntimeException("Failed to fetch issues by status", e);
        }
    }

    @Override
    public IssueResponseDTO getIssueDetailsById(Long id) {
        try {
            log.info("Fetching issue details for ID: {}", id);

            Issue issue = issueRepository.findByIdWithProject(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Issue not found with ID: " + id));

            return new IssueResponseDTO(
                    issue.getId(),
                    issue.getTitle(),
                    issue.getDescription(),
                    issue.getStatus(),
                    issue.getPriority(),
                    issue.getProject().getName()
            );

        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("Unexpected error while fetching issue details for ID: {}", id, e);
            throw new RuntimeException("Failed to fetch issue details", e);
        }
    }

    @Override
    public List<Issue> getIssuesByEmployeeId(Long employeeId) {
        try {
            log.info("Fetching issues assigned to employee ID: {}", employeeId);
            return issueRepository.findByAssignedToEmployeeId(employeeId);
        } catch (Exception e) {
            log.error("Error fetching issues for employee ID: {}", employeeId, e);
            throw new RuntimeException("Failed to fetch issues for employee", e);
        }
    }
}
