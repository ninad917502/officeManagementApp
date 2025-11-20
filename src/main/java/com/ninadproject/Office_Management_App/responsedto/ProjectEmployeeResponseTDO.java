package com.ninadproject.Office_Management_App.responsedto;

import lombok.Data;

@Data
public class ProjectEmployeeResponseTDO {

	private Long employeeId;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

}
