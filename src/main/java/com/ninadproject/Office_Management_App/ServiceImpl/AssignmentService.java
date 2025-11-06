package com.ninadproject.Office_Management_App.ServiceImpl;

import com.ninadproject.Office_Management_App.Entity.ProjectAssignment;
import com.ninadproject.Office_Management_App.Repository.ProjectAssignmentRepository;
import com.ninadproject.Office_Management_App.dto.ProjectAssignmentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AssignmentService {

    @Autowired
    private ProjectAssignmentRepository assignmentRepository;

    public List<ProjectAssignmentDTO> getAssignmentsByEmployeeId(Long employeeId) {
        try {
            log.info("Fetching project assignments for employee ID: {}", employeeId);

            List<ProjectAssignment> assignments = assignmentRepository.findByEmployeeEmployeeId(employeeId);
            return assignments.stream()
                    .map(a -> new ProjectAssignmentDTO(
                            a.getProject().getName(),
                            a.getStartDate(),
                            a.getEndDate()
                    ))
                    .collect(Collectors.toList());


        } catch (Exception e) {
            log.error("Error fetching assignments for employee ID: {}", employeeId, e);
            throw new RuntimeException("Failed to fetch project assignments", e);
        }
    }
}
