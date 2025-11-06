package com.ninadproject.Office_Management_App.ServiceImpl;

import com.ninadproject.Office_Management_App.Entity.AttendanceRecord;
import com.ninadproject.Office_Management_App.Entity.Employee;
import com.ninadproject.Office_Management_App.Repository.AttendanceRepository;
import com.ninadproject.Office_Management_App.Repository.EmployeeRepository;
import com.ninadproject.Office_Management_App.dto.AttendanceGridDTO;
import com.ninadproject.Office_Management_App.dto.AttendanceRecordDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public String saveAll(List<AttendanceRecordDTO> records) {
       try {
           log.info("saving attendance for {} records: ", records.size());

           for (AttendanceRecordDTO dto : records) {
               AttendanceRecord record = new AttendanceRecord();
               record.setDate(dto.getDate());
               record.setStatus(dto.getStatus());

              // Convert empId → Employee
              Employee emp = employeeRepository.findById(dto.getEmpId())
                      .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + dto.getEmpId()));
              record.setEmployee(emp);

               attendanceRepository.save(record);
               log.debug("Saved attendance for employee ID: {}, Date: {}", emp.getEmployeeId(), dto.getDate());

           }
           return "Attendance saved successfully";
       } catch (Exception e) {
           log.error("Error while saving attendance records", e);
           throw new RuntimeException("Failed to save attendance", e);
       }
    }

    public List<AttendanceRecord> getByDate(LocalDate date) {
        try {
            log.info("Fetching attendance for date: {}", date);
            return attendanceRepository.findByDate(date);
        } catch (Exception e) {
            log.error("Error fetching attendance for date: {}", date, e);
            throw new RuntimeException("Failed to fetch attendance by date", e);
        }
    }

    public List<AttendanceRecord> getByEmployee(Long empId) {
        try {
            log.info("Fetching attendance for employee ID: {}", empId);
            return attendanceRepository.findByEmployeeEmployeeId(empId);
        } catch (Exception e) {
            log.error("Error fetching attendance for employee ID: {}", empId, e);
            throw new RuntimeException("Failed to fetch attendance for employee", e);
        }
    }
    public List<AttendanceGridDTO> getAttendanceGrid(int month, int year) {
        try {
            log.info("Generating attendance grid for Month: {}, Year: {}", month, year);


            List<Employee> employees = employeeRepository.findAll();
        List<AttendanceRecord> records = attendanceRepository.findByMonthAndYear(month, year);

        Map<Long, Map<String, String>> empAttendanceMap = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (AttendanceRecord record : records) {
        	Employee emp = record.getEmployee();
            if (emp == null) continue; // skip invalid data

            Long empId = emp.getEmployeeId();
            String dateStr = record.getDate().format(formatter);
            empAttendanceMap
                .computeIfAbsent(empId, k -> new HashMap<>())
                .put(dateStr, record.getStatus().name());
        }

        List<AttendanceGridDTO> result = new ArrayList<>();
        for (Employee emp : employees) {
            Map<String, String> att = empAttendanceMap.getOrDefault(emp.getEmployeeId(), new HashMap<>());

            int p = 0, a = 0, h = 0;
            for (String val : att.values()) {
                switch (val) {
                    case "P" -> p++;
                    case "A" -> a++;
                    case "H" -> h++;
                }
            }

            result.add(new AttendanceGridDTO(
                emp.getEmployeeId(),
                emp.getFirstName() + " " + emp.getLastName(),
               
                att, p, a, h
            ));
        }

            log.info("Attendance grid generated with {} rows", result.size());
            return result;

        } catch (Exception e) {
            log.error("Error generating attendance grid for month: {} and year: {}", month, year, e);
            throw new RuntimeException("Failed to generate attendance grid", e);
        }
    }

    public List<AttendanceGridDTO> getAttendanceGrid(int month, int year,long employeeId) {
        try {
            log.info("Generating attendance grid for Month: {}, Year: {}", month, year);


            List<Employee> employees = employeeRepository.findAll();
        List<AttendanceRecord> records = attendanceRepository.findByMonthAndYear(month, year);

        Map<Long, Map<String, String>> empAttendanceMap = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (AttendanceRecord record : records) {
        	Employee emp = record.getEmployee();
            if (emp == null) continue; // skip invalid data

            Long empId = emp.getEmployeeId();
            String dateStr = record.getDate().format(formatter);
            empAttendanceMap
                .computeIfAbsent(empId, k -> new HashMap<>())
                .put(dateStr, record.getStatus().name());
        }

        List<AttendanceGridDTO> result = new ArrayList<>();
        for (Employee emp : employees) {
            Map<String, String> att = empAttendanceMap.getOrDefault(emp.getEmployeeId(), new HashMap<>());

            int p = 0, a = 0, h = 0;
            for (String val : att.values()) {
                switch (val) {
                    case "P" -> p++;
                    case "A" -> a++;
                    case "H" -> h++;
                }
            }

            if (employeeId==emp.getEmployeeId()) {
            result.add(new AttendanceGridDTO(
                emp.getEmployeeId(),
                emp.getFirstName() + " " + emp.getLastName(),
               
                att, p, a, h
            ));}
        }

            log.info("Attendance grid generated with {} rows", result.size());
            return result;

        } catch (Exception e) {
            log.error("Error generating attendance grid for month: {} and year: {}", month, year, e);
            throw new RuntimeException("Failed to generate attendance grid", e);
        }
    }

   public void  updateAttendanceByEmployeeId( AttendanceRecordDTO records) {
   }
}
