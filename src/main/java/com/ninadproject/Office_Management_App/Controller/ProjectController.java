package com.ninadproject.Office_Management_App.Controller;

import com.ninadproject.Office_Management_App.Entity.Employee;
import com.ninadproject.Office_Management_App.Entity.Project;
import com.ninadproject.Office_Management_App.Repository.EmployeeRepository;
import com.ninadproject.Office_Management_App.ServiceImpl.ProjectServiceImpl;
import com.ninadproject.Office_Management_App.dto.AssignmentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = {"http://localhost:5174", "http://localhost:5173", "http://localhost:5175"})
public class ProjectController {

    @Autowired
    private ProjectServiceImpl projectService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Project project, Principal principal) {
        try {
            String name = project.getName();
            if(!name.matches("^(?!\\d)[A-Za-z][A-Za-z0-9 ]{2,}$")){
                return ResponseEntity.badRequest().body("Invalid project name: Give Good Valid Project Name");
            }

            Project saved = projectService.create(project, principal);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            log.error("Project creation failed", e);
            return ResponseEntity.internalServerError().body("Project creation failed");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(Principal principal) {
        try {
            return ResponseEntity.ok(projectService.getAllCreatedByUser(principal));
        } catch (Exception e) {
            log.error("Fetching projects failed", e);
            return ResponseEntity.internalServerError().body("Failed to fetch projects");
        }
    }

    @PutMapping("/{projectId}/assign")
    public ResponseEntity<?> assignEmployeeToProject(@PathVariable Long projectId, @RequestBody AssignmentRequest request) {
        try {
            String result = projectService.assignEmployee(projectId, request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Assignment failed", e);
            return ResponseEntity.internalServerError().body("Failed to assign employee");
        }
    }

    @GetMapping("/my-projects")
    public ResponseEntity<?> getMyProjects(Principal principal) {
        try {
            String username = principal.getName();
            Employee employee = employeeRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            return ResponseEntity.ok(projectService.getProjectsAssignedTo(employee));
        } catch (Exception e) {
            log.error("Error fetching assigned projects", e);
            return ResponseEntity.internalServerError().body("Could not fetch assigned projects");
        }

    }
    @GetMapping("/getProject/{employeeId}")
    public ResponseEntity<?> getAssignedProjects(@PathVariable Long employeeId) {
        try {
            log.info("Fetching assigned projects for employee ID: {}", employeeId);
            List<String> projects = projectService.getProjectsAssignedTo(employeeId);
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            log.error("Error fetching assigned projects for employee ID: {}", employeeId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to fetch assigned projects\n"+e.getMessage());
        }
	   }
}
