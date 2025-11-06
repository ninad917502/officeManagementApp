package com.ninadproject.Office_Management_App.ServiceImpl;

import com.ninadproject.Office_Management_App.Entity.AddressDetails;
import com.ninadproject.Office_Management_App.Entity.Employee;
import com.ninadproject.Office_Management_App.Repository.EmployeeRepository;
import com.ninadproject.Office_Management_App.Service.EmployeeService;
import com.ninadproject.Office_Management_App.exception.DataAccessException;
import com.ninadproject.Office_Management_App.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
    private final EmployeeRepository repository;

   private static final  PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        // Link back AddressDetails and PersonalDetails to employee

        try {
            log.info("Creating employee: {}", employee.getUsername());

            if (repository.existsByUsername(employee.getUsername())) {
                throw new RuntimeException("Username already exists: " + employee.getUsername());
            }

            if (repository.existsByEmail(employee.getEmail())) {
                throw new RuntimeException("Email already exists: " + employee.getEmail());
            }

            // encode password
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));

            return repository.save(employee);
        } catch (Exception e) {
            log.error("Error while creating employee: {}", employee.getUsername(), e);
            throw new RuntimeException("Failed to create employee", e);
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        try {
            log.info("Fetching all employees");
            return repository.findAll();
        } catch (Exception e) {
            log.error("Error fetching all employees", e);
            throw new RuntimeException("Failed to fetch employees", e);
        }
    }

    @Override
    public Employee getEmployeeById(Long id) {
        try {
            log.info("Fetching employee by ID: {}", id);
            return repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
        } catch (Exception e) {
            log.error("Error fetching employee by ID: {}", id, e);
            throw new RuntimeException("Failed to fetch employee", e);
        }
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {

        try {
            log.info("Updating employee ID: {}", id);
        Employee existing = getEmployeeById(id);

        existing.setUsername(employee.getUsername());
        existing.setEmail(employee.getEmail());
        existing.setFirstName(employee.getFirstName());
        existing.setLastName(employee.getLastName());
        existing.setPassword(employee.getPassword() != null && !employee.getPassword().isEmpty()
        ?passwordEncoder.encode(employee.getPassword()) : existing.getPassword());
        existing.setRoles(employee.getRoles());
        existing.setActive(employee.isActive());


        if (employee.getAddresses() != null) {
//            employee.getAddresses().forEach(addr -> addr.setEmployee(existing));
//            existing.setAddresses(employee.getAddresses());
//
//             Clear existing addresses to allow orphanRemoval
            existing.getAddresses().clear();

            // Add updated addresses
            for (AddressDetails addr : employee.getAddresses()) {
                addr.setEmployee(existing); // Set back-reference
                existing.getAddresses().add(addr); // Add to managed collection
            }
        }

        if (employee.getPersonalDetails() != null) {
            employee.getPersonalDetails().setEmployee(existing);
            existing.setPersonalDetails(employee.getPersonalDetails());
        }

        return repository.save(existing);
        } catch (Exception e) {
        log.error("Error updating employee ID: {}", id, e);
        throw new RuntimeException("Failed to update employee", e);
        }
    }

    @Override
    @Transactional
    public String softdeleteEmployee(Long id) {
         Employee employee = repository.findByEmployeeId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Not Found with this id: " + id));
         employee.setActive(false);
         repository.save(employee);

        return "Employee is Inactive";
    }

    @Override
    public Employee addEmployee(Employee employee) {
        try {
            log.info("Adding employee: {}", employee.getUsername());
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
            return repository.save(employee);
        } catch (Exception e) {
            log.error("Failed to add employee", e);
            throw new RuntimeException("Failed to add employee", e);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        try {
            log.info("Checking if username exists: {}", username);
            boolean exists = repository.existsByUsername(username);
            log.debug("Username {} exists: {}", username, exists);
            return exists;
        } catch (DataAccessException ex) {
            log.error("Database error while checking username {}: {}", username, ex.getMessage(), ex);
            throw new RuntimeException("Database error occurred while checking username.", ex);
        } catch (Exception ex) {
            log.error("Unexpected error while checking username {}: {}", username, ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error occurred while checking username.", ex);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
       try {
            log.info("Checking if email exists: {}", email);
            boolean exists = repository.existsByEmail(email);
            log.debug("Email {} exists: {}", email, exists);
            return exists;
        } catch (DataAccessException ex) {
            log.error("Database error while checking email {}: {}", email, ex.getMessage(), ex);
            throw new RuntimeException("Database error occurred while checking email.", ex);
        } catch (Exception ex) {
            log.error("Unexpected error while checking email {}: {}", email, ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error occurred while checking email.", ex);
        }
    }

    @Override
    public void deleteEmployee(Long id) {
      try {
          log.info("deleting Employees by Id: {}", id);
          repository.deleteById(id);

      } catch (Exception e) {
          log.error("Error while deleting employees by Id: {} ", id, e);
          throw new RuntimeException("Failed to Delete Employee",e);
      }

    }
    public boolean deactivateEmployee(Long id) {
        Optional<Employee> optionalEmployee = repository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setActive(false);
            repository.save(employee);
            return true;
        } else {
            return false;
        }
    }
    @Override
	public Employee getEmployeeByUserName(String userName) {
	      Optional<Employee> optionalEmployee = repository.findByUsername(userName);
		return optionalEmployee.get();
	}

    @Override
    public String updatePassword(String username, String newPassword) {
       try {
           log.info("received request to update new password for username: {}", username);
           Employee employee = repository.findByUsername(username).orElseThrow(() -> {
               log.error("user not found with username : {}", username);
               return new ResourceNotFoundException("User Not Found With this Username" + username);
           });
           employee.setPassword(passwordEncoder.encode(newPassword));
           repository.save(employee);

           log.info("Password updated successfully for username: {}", username);
           return "Password updated successfully";
       } catch (Exception e) {
           log.error("error while updating password for username {}: {} ", username , e.getMessage() , e );
           throw new RuntimeException("unexcepted error while updating password", e);
       }
    }
	
}