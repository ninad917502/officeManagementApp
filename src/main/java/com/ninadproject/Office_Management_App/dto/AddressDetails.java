package com.ninadproject.Office_Management_App.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class AddressDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    private String street;
    private String city;
    private String state;
    private String pinCode;
    private String houseNo;
    private String buildingName;
    @Column(nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "employeeId")
    @JsonBackReference("employee-address")
    private Employee employee;


}