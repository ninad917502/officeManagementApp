package com.ninadproject.Office_Management_App.Repository;

import com.ninadproject.Office_Management_App.Entity.Issue;
import com.ninadproject.Office_Management_App.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByProject(Project project);

	List<Issue> findByAssignedToEmployeeId(Long userId);
	@Query("SELECT i FROM Issue i WHERE i.assignedTo.id = :userId AND i.project.project_id = :projectId")
	List<Issue> findByUserIdAndProjectId(@Param("userId") Long userId, @Param("projectId") Long projectId);

	List<Issue> findByStatus(String status);

	@Query("SELECT i FROM Issue i JOIN FETCH i.project WHERE i.id = :id")
	Optional<Issue> findByIdWithProject(@Param("id") Long id);
}