package com.ninadproject.Office_Management_App.ServiceImpl;

import com.ninadproject.Office_Management_App.Entity.Employee;
import com.ninadproject.Office_Management_App.Entity.Project;
import com.ninadproject.Office_Management_App.Entity.ProjectAssignment;
import com.ninadproject.Office_Management_App.Repository.EmployeeRepository;
import com.ninadproject.Office_Management_App.Repository.ProjectAssignmentRepository;
import com.ninadproject.Office_Management_App.Repository.ProjectRepository;
import com.ninadproject.Office_Management_App.Service.ProjectService;
import com.ninadproject.Office_Management_App.dto.AssignmentRequest;
import com.ninadproject.Office_Management_App.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository userRepository;

    @Autowired
    private ProjectAssignmentRepository assignmentRepository;

    @Override
    public Project create(Project project, Principal principal) {
        try {
            log.info("Creating project by user: {}", principal.getName());


            Employee creator = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            project.setCreatedBy(creator);
            Project saved = projectRepository.save(project);
            log.info("Project created by user {} with ID {}", principal.getName(), saved.getProject_id());
            return saved;
        }catch (Exception e) {
            log.error("Failed to create project for user: {}", principal.getName(), e);
            throw new RuntimeException("Project creation failed", e);
        }
    }

    @Override
    public List<Project> getAllCreatedByUser(Principal principal) {
       try{
           log.info("Fetching all projects created by user: {}", principal.getName());

           Employee user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

           List<Project> projects = projectRepository.findByCreatedBy(user);
           log.info("Found {} projects for user: {}", projects.size(), user.getUsername());

           return projects;

    }catch (Exception e) {
           log.error("Error fetching projects for user: {}", principal.getName(), e);
           throw new RuntimeException("Failed to fetch user projects", e);
       }

    }

    @Override
    public String assignEmployee(Long projectId, AssignmentRequest request) {
        try {
            log.info("Assigning employee {} to project {}", request.getUserId(), projectId);

            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

            Employee employee = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

            boolean alreadyAssigned = assignmentRepository
                    .findByEmployeeEmployeeId(employee.getEmployeeId())
                    .stream()
                    .anyMatch(pa -> pa.getProject().getProject_id().equals(projectId) && pa.getEndDate() == null);

            if (alreadyAssigned) {
                log.warn("Employee {} already assigned to project {}", employee.getEmployeeId(), projectId);
                return "Employee is already assigned to this project.";
            }

            ProjectAssignment assignment = new ProjectAssignment();
            assignment.setEmployee(employee);
            assignment.setProject(project);
            assignment.setStartDate(LocalDate.now());

//            List<Employee> assignedList = new ArrayList<>();
//            assignedList.add(employee); // or multiple employees
//            project.setAssignedTo(assignedList);
//
//            if (project.getTeamAssignments() == null) {
//                project.setTeamAssignments(new ArrayList<>());
//            }
//            project.getTeamAssignments().add(assignment);
//
//            if (employee.getAssignments() == null) {
//                employee.setAssignments(new ArrayList<>());
//            }
//            employee.getAssignments().add(assignment);

           // projectRepository.save(project);

            assignmentRepository.save(assignment);
            log.info("Employee {} assigned to project {}", employee.getEmployeeId(), projectId);
            return "Employee assigned to project successfully";
        } catch (Exception e) {
            log.error("Failed to assign employee {} to project {}", request.getUserId(), projectId, e);
            throw new RuntimeException("Employee assignment failed", e);
        }
    }

    @Override
    public List<Project> getProjectsAssignedTo(Employee employee) {
        try {
            log.info("Fetching projects assigned to employee ID: {}", employee.getEmployeeId());
            List<Project> projects = projectRepository.findByAssignedToContains(employee);
            log.info("Found {} assigned projects for employee ID: {}", projects.size(), employee.getEmployeeId());
            return projects;
        } catch (Exception e) {
            log.error("Error fetching assigned projects for employee ID: {}", employee.getEmployeeId(), e);
            throw new RuntimeException("Failed to fetch assigned projects", e);
        }
    }

    @Override
    public List<String> getProjectsAssignedTo(Long employeeId) {

         List<ProjectAssignment> assignments = assignmentRepository.findByEmployeeEmployeeId(employeeId);
        if (assignments.isEmpty()) {
            throw new ResourceNotFoundException("No projects found for employee ID: " + employeeId);
        }
        return assignments.stream()
               .map(pro->pro.getProject().getName())
               .distinct()
               .toList();

    }

}
