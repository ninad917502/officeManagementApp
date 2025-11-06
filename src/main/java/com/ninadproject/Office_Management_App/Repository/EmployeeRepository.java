package com.ninadproject.Office_Management_App.Repository;

import com.ninadproject.Office_Management_App.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);
    Optional<Employee> findByEmployeeId(long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    List<Employee> findByActiveTrue();
}