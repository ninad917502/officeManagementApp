package com.ninadproject.Office_Management_App.Service;

import com.ninadproject.Office_Management_App.Entity.Employee;

import java.util.List;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    Employee updateEmployee(Long id, Employee updatedEmployee);
    void deleteEmployee(Long id);
	 String softdeleteEmployee(Long id);
    Employee addEmployee(Employee employee);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    public boolean deactivateEmployee(Long id);
    public Employee getEmployeeByUserName(String userName);
     String updatePassword(String username, String newPassword);
   // List<Project> getProjectsAssignedTo(Long employeeId);
}