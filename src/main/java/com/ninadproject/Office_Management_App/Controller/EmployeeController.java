package com.ninadproject.Office_Management_App.Controller;

import com.ninadproject.Office_Management_App.Entity.Employee;
import com.ninadproject.Office_Management_App.Service.EmployeeService;
import com.ninadproject.Office_Management_App.ServiceImpl.AssignmentService;
import com.ninadproject.Office_Management_App.dto.PasswordChangeRequest;
import com.ninadproject.Office_Management_App.dto.ProjectAssignmentDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = {"http://localhost:5174", "http://localhost:5173",  "http://localhost:5175"})
public class EmployeeController {

	   @Autowired
	    private AssignmentService assignmentService;

       @Autowired
       private PasswordEncoder passwordEncoder;
       @Autowired
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService service) {
        this.employeeService = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createEmployee(
            @RequestPart("form") String employeeJson,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {

        try {
            log.info("Creating new employee JSON: {}", employeeJson);

            // Convert JSON to Employee object
            ObjectMapper mapper = new ObjectMapper();
            Employee employee = mapper.readValue(employeeJson, Employee.class);

            // 🔐 Encrypt the password
  if (employeeService.existsByUsername(employee.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Username already exists");
            }

            if (employeeService.existsByEmail(employee.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Email already registered try with new Email ");
            }
            if (employee.getPassword() != null && !employee.getPassword().isBlank()) {
                String hashedPassword = passwordEncoder.encode(employee.getPassword());
                employee.setPassword(hashedPassword);
            }

            // Process photo if provided
            if (photo != null && !photo.isEmpty()) {
                byte[] imageData = photo.getBytes();
                employee.setPhoto(imageData);
                log.debug("Photo uploaded for employee: {}", employee.getUsername());
            }

            // Save to DB
            Employee savedEmployee = employeeService.createEmployee(employee);
            log.info("Employee created with ID: {}", savedEmployee.getEmployeeId());
            return ResponseEntity.ok(savedEmployee);

        } catch (Exception e) {
            log.error("Failed to Create Employee", e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        try{
            log.info("Fetching All Data");
            return ResponseEntity.ok(employeeService.getAllEmployees());
        } catch (Exception e) {
            log.error("Error fetching employees ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        try{
            log.info("Fetching employee with ID: {}",id);
            Employee employee = employeeService.getEmployeeById(id);
            return ResponseEntity.ok(employee);

        } catch (Exception e) {
            log.error("Error fetching employee by ID: {}",id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @GetMapping("/userName/{userName}")
    public ResponseEntity<Employee> getEmployeeByUserName(@PathVariable String userName) {
        try{
            log.info("Fetching employee with ID: {}",userName);
            Employee employee = employeeService.getEmployeeByUserName(userName);
            return ResponseEntity.ok(employee);

        } catch (Exception e) {
            log.error("Error fetching employee by ID: {}",userName, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
    	 boolean result = employeeService.deactivateEmployee(id);
    	    if (result) {
    	        return ResponseEntity.ok("Employee deactivated successfully.");
    	    } else {
    	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");
    	    }
    }
    
    @GetMapping("/{id}/assignments")
    public List<ProjectAssignmentDTO> getEmployeeAssignments(@PathVariable Long id) {
        return assignmentService.getAssignmentsByEmployeeId(id);
    }
    @GetMapping("/me")
	public ResponseEntity<Employee> getCurrentUser(Authentication authentication) {
	    String username = authentication.getName();
	    System.out.println(username+"|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
	    Employee user = employeeService.getEmployeeByUserName(username);
	    return ResponseEntity.ok(user);
	}

    @PutMapping("/update_pwd")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordChangeRequest request) {
        String message = employeeService.updatePassword(request.getUsername(), request.getNewPassword());
        return ResponseEntity.ok(message);
    }
}