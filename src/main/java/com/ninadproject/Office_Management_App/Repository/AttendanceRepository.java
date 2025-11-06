package com.ninadproject.Office_Management_App.Repository;

import com.ninadproject.Office_Management_App.Entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByDate(LocalDate date);
    List<AttendanceRecord> findByEmployeeEmployeeId(Long employeeId);
    @Query("SELECT a FROM AttendanceRecord a WHERE MONTH(a.date) = :month AND YEAR(a.date) = :year")
    List<AttendanceRecord> findByMonthAndYear(@Param("month") int month, @Param("year") int year);

}