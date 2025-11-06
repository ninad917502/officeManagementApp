package com.ninadproject.Office_Management_App.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
public class PersonalDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long personalId;
    private String bloodGroup;
    private Boolean policeVerification;
    private String emergencyContactNumber;
    private String aadharCardNumber;
    private String panCardNumber;
    private String workLocation;
    private String bankAccountNumber;
    private String ifscCode;
    private String assetsDetails;
    private String source;
    private String pfAccountNumber;
    private Double salary;
    private String personalEmailId;
    private String officialEmailId;
    private String designation;
    private Date dateOfBirth;
    private Date dateOfJoining;
    @OneToOne(mappedBy = "personalDetails")
    @JsonBackReference("personalDetails")
    private Employee employee;


}