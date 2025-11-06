package com.ninadproject.Office_Management_App.Controller;

import com.ninadproject.Office_Management_App.Entity.AttendanceRecord;
import com.ninadproject.Office_Management_App.Entity.Employee;
import com.ninadproject.Office_Management_App.Service.EmployeeService;
import com.ninadproject.Office_Management_App.ServiceImpl.AttendanceService;
import com.ninadproject.Office_Management_App.dto.AttendanceGridDTO;
import com.ninadproject.Office_Management_App.dto.AttendanceRecordDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = {"http://localhost:5174", "http://localhost:5173", "http://localhost:5175"})
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/mark")
    public ResponseEntity<String> markAttendance(@RequestBody List<AttendanceRecordDTO
    		> records) {
        try{
            log.info("Marking attendance for {} records", records.size());
            String result = attendanceService.saveAll(records);
            return ResponseEntity.ok(result);
        }catch (Exception e ) {
            log.error("Error marking attendance ");
            return ResponseEntity.internalServerError().body("Failed to marks attendance");
        }
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<?> getAttendanceForDate(@PathVariable String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            log.info("Fetching attendance for date: {}", localDate);
            List<AttendanceRecord> records = attendanceService.getByDate(localDate);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            log.error("Error fetching attendance for date: {}", date, e);
            return ResponseEntity.badRequest().body("Invalid date or data not found");
        }
    }
    
    @GetMapping("/grid")
    public ResponseEntity<?> getGrid(@RequestParam int month, @RequestParam int year) {
        try {
            log.info("Generating attendance grid for month: {}, year: {}", month, year);
            List<AttendanceGridDTO> grid = attendanceService.getAttendanceGrid(month , year);
            return ResponseEntity.ok(grid);
        } catch (Exception e) {
            log.error("Error generating attendance grid", e);
            return ResponseEntity.internalServerError().body("Failed to generate attendance grid");
        }
    }
    
    @GetMapping("/gridByEmp")
    public ResponseEntity<?> getGrid(@RequestParam int month, @RequestParam int year,Principal username) {
        try {
        	
        	Employee employee= employeeService.getEmployeeByUserName(username.getName());
            log.info("Generating attendance grid for month: {}, year: {}", month, year);
            List<AttendanceGridDTO> grid = attendanceService.getAttendanceGrid(month , year, employee.getEmployeeId() );
            return ResponseEntity.ok(grid);
        } catch (Exception e) {
            log.error("Error generating attendance grid", e);
            return ResponseEntity.internalServerError().body("Failed to generate attendance grid");
        }
    }
    
//    @PostMapping("/mark")
//    public ResponseEntity<String> updateAttendanceByEmployeeId(@RequestBody AttendanceRecordDTO
//    		 records) {
//        try{
//            log.info("Marking attendance for {} records", records);
//            String result = attendanceService.updateAttendanceByEmployeeId(records);
//            return ResponseEntity.ok(result);
//        }catch (Exception e ) {
//            log.error("Error marking attendance ");
//            return ResponseEntity.internalServerError().body("Failed to marks attendance");
//        }
//    }

    
}