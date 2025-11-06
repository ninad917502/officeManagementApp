package com.ninadproject.Office_Management_App.Service;

import com.ninadproject.Office_Management_App.Entity.Employee;
import com.ninadproject.Office_Management_App.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository userRepository;

    @Override
 
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee emp = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<SimpleGrantedAuthority> authorities = emp.getRoles().stream()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                emp.getUsername(),
                emp.getPassword(),
                authorities
        );
    }

}
