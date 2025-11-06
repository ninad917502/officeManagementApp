package com.ninadproject.Office_Management_App.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class AttendanceGridDTO {
    private Long employeeId;
    private String employeeName;
    
    private Map<String, String> attendance;

    private int presentCount;
    private int absentCount;
    private int holidayCount;
    
    public AttendanceGridDTO() {}

    public AttendanceGridDTO(Long id, String name, Map<String, String> att,
                             int p, int a, int h) {
        this.employeeId = id;
        this.employeeName = name;
       
        this.attendance = att;
        this.presentCount = p;
        this.absentCount = a;
        this.holidayCount = h;
    }
    
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	
	public Map<String, String> getAttendance() {
		return attendance;
	}
	public void setAttendance(Map<String, String> attendance) {
		this.attendance = attendance;
	}
	public int getPresentCount() {
		return presentCount;
	}
	public void setPresentCount(int presentCount) {
		this.presentCount = presentCount;
	}
	public int getAbsentCount() {
		return absentCount;
	}
	public void setAbsentCount(int absentCount) {
		this.absentCount = absentCount;
	}
	public int getHolidayCount() {
		return holidayCount;
	}
	public void setHolidayCount(int holidayCount) {
		this.holidayCount = holidayCount;
	}
    
    
}