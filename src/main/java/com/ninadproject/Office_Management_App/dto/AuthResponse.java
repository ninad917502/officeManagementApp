package com.ninadproject.Office_Management_App.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponse {

	private String token;

	public AuthResponse(String token) {
		super();
		this.token = token;
	}
	
}
