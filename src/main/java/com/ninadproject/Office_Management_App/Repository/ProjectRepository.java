package com.ninadproject.Office_Management_App.Repository;

import com.ninadproject.Office_Management_App.Entity.Employee;
import com.ninadproject.Office_Management_App.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByCreatedBy(Employee user);
    List<Project> findByAssignedToContains(Employee user);
}