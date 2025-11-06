package com.ninadproject.Office_Management_App.Controller;

import com.ninadproject.Office_Management_App.Entity.Employee;
import com.ninadproject.Office_Management_App.Repository.EmployeeRepository;
import com.ninadproject.Office_Management_App.Service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins =  {"http://localhost:5174", "http://localhost:5173", "http://localhost:5175"})
public class UserController {

	@Autowired
	EmployeeRepository userRepository;

	@Autowired
	  private  EmployeeService employeeService;
	@GetMapping("/getUsers")
	public ResponseEntity<?> getEmployee() {
		try{
			log.info("Fetch all users ");
			List<Employee> users = userRepository.findAll();
			return ResponseEntity.ok(users);
		} catch (Exception e) {
			log.error("Error while fetching users", e);
			return ResponseEntity.internalServerError().body("Failed to fetch users");
		}
	}

	@PostMapping
	public ResponseEntity<?> addEmployee(@RequestBody Employee e) {

		try {
			log.info("Adding new employee: {}", e.getUsername());
			Employee saved = employeeService.addEmployee(e);
			return ResponseEntity.ok(saved);
		} catch (Exception ex) {
			log.error("Error adding employee", ex);
			return ResponseEntity.internalServerError().body("Failed to add user");
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Employee employee) {
		try {
			log.info("Updating employee ID: {}", id);
			employee.setEmployeeId(id);
			Employee updated = userRepository.save(employee);
			return ResponseEntity.ok(updated);
		} catch (Exception e) {
			log.error("Error updating employee ID: {}", id, e);
			return ResponseEntity.internalServerError().body("Failed to update employee");
		}
	}
	@GetMapping("/me")
	public ResponseEntity<Employee> getCurrentUser(Authentication authentication) {
	    String username = authentication.getName();
	    System.out.println(username+"|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
	    Optional user = userRepository.findByUsername(username);
	    return ResponseEntity.ok((Employee)user.get());
	}
}
