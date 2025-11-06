package com.ninadproject.Office_Management_App;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.ninadproject.Office_Management_App.Repository")
@EntityScan(basePackages = "com.ninadproject.Office_Management_App.Entity")
public class OfficeManagementAppApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(OfficeManagementAppApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(OfficeManagementAppApplication.class);
	}
}
