package com.ninadproject.Office_Management_App.Repository;

import com.ninadproject.Office_Management_App.Entity.ProjectAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignment, Long> {
    List<ProjectAssignment> findByEmployeeEmployeeId(Long employeeId);
}
