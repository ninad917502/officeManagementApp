package com.ninadproject.Office_Management_App.Controller;

import com.ninadproject.Office_Management_App.Entity.Issue;
import com.ninadproject.Office_Management_App.ServiceImpl.IssueServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins =  {"http://localhost:5174", "http://localhost:5173",  "http://localhost:5175"})
public class IssueController {

 @Autowired
    private IssueServiceImpl issueService;


    @PostMapping("/create")
    public ResponseEntity<Issue> create(@RequestBody Issue issue, Principal principal) {
        try {
            Issue saved = issueService.createIssue(issue, principal);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            log.error("Failed to create issue", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getByProject(@PathVariable Long projectId) {
        try {
            return ResponseEntity.ok(issueService.getIssuesByProject(projectId));
        } catch (Exception e) {
            log.error("Failed to get issues for project {}", projectId, e);
            return ResponseEntity.badRequest().body("This project dose not have any issue so Failed to get issues");
        }
    }

    
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        try {
            String newStatus = statusMap.get("status");
            return ResponseEntity.ok(issueService.updateStatus(id, newStatus));
        } catch (Exception e) {
            log.error("Failed to update status for issue {}", id, e);
            return ResponseEntity.internalServerError().body("Failed to update status");
        }
    }
    @GetMapping("/user/{employeeId}")
    public ResponseEntity<?> getIssuesByUser(@PathVariable Long employeeId) {
        try {
            return ResponseEntity.ok(issueService.getIssuesByUser(employeeId));
        } catch (Exception e) {
            log.error("Failed to get issues for employee {}", employeeId, e);
            return ResponseEntity.internalServerError().body("Failed to fetch issues");
        }
    }
    @GetMapping("/user/{userId}/{projectId}")
    public ResponseEntity<?> getByUserAndProject(@PathVariable Long userId, @PathVariable Long projectId) {
        try {
            return ResponseEntity.ok(issueService.getIssuesByUserAndProject(userId, projectId));
        } catch (Exception e) {
            log.error("Failed to get issues for user {} and project {}", userId, projectId, e);
            return ResponseEntity.internalServerError().body("Failed to fetch issues");
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getIssuesByStatus(@PathVariable String status) {
        try {
            return ResponseEntity.ok(issueService.getIssuesByStatus(status));
        } catch (Exception e) {
            log.error("Failed to get issues by status {}", status, e);
            return ResponseEntity.internalServerError().body("Failed to fetch issues with this status" + status);
        }
    }


    @GetMapping("/getIssue/{issueId}")
    public ResponseEntity<?> getIssueDetails(@PathVariable Long issueId) {
        try {
            return ResponseEntity.ok(issueService.getIssueDetailsById(issueId));
        } catch (Exception e) {
            log.error("Error fetching issue by ID: {}", issueId, e);
            return ResponseEntity.internalServerError().body("Could not fetch issue details with this Id" + issueId);
        }
    }

    @GetMapping("/getIssues/{employeeId}")
    public ResponseEntity<?> getIssuesByEmployeeId(@PathVariable Long employeeId) {
        try {
            List<Issue> issues = issueService.getIssuesByEmployeeId(employeeId);
            return ResponseEntity.ok(issues);
        } catch (Exception e) {
            log.error("Failed to get issues for employee ID: {}", employeeId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to get issues for employee");
        }
    }
}
