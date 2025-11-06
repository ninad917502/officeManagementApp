package com.ninadproject.Office_Management_App.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
@Data
@Entity
@Table(name = "employee")
public class Employee implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long employeeId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String firstName;
    private String lastName;

    @Lob
    @Column(name = "photo", columnDefinition = "LONGBLOB")
    private byte[] photo;

    @ElementCollection(fetch = FetchType.EAGER) // fetch roles eagerly
    @CollectionTable(name = "employee_roles", joinColumns = @JoinColumn(name = "employeeId"))
    @Column(name = "role")
    private List<String> roles = new ArrayList<>();

    @ManyToMany(mappedBy = "assignedTo")
    private List<Project> assignedProjects;

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    private List<Project> createdProjects;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("employee-address")
    private List<AddressDetails> addresses = new ArrayList<>();

    
    @Column(name = "is_active")
    private Boolean  isActive = true;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_id")
    @JsonManagedReference("personalDetails")
    private PersonalDetails personalDetails;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonManagedReference("assignments")
    private List<ProjectAssignment> assignments;

    public Employee() {}

    public Employee(Long id, String username, String password, List<String> roles) {
        this.employeeId = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    // === UserDetails implementation ===
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return isActive; }
    @Override public boolean isAccountNonLocked() { return isActive; }
    @Override public boolean isCredentialsNonExpired() { return isActive; }
    @Override public boolean isEnabled() { return isActive; }

    // === Getters and Setters ===

    public Boolean  isActive() {
		return isActive;
	}

	public void setActive(Boolean  isActive) {
		this.isActive = isActive;
	}
    
    
}
