package com.ApplyMate.ApplyMate.repository;

import com.ApplyMate.ApplyMate.entity.JobApplication;
import com.ApplyMate.ApplyMate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long>, JpaSpecificationExecutor<JobApplication> {
    @Query("SELECT DISTINCT ja FROM JobApplication ja " +
            "LEFT JOIN FETCH ja.interviews " +
            "WHERE ja.user = :user " +
            "ORDER BY ja.applicationDate DESC")
    List<JobApplication> findByUser(@Param("user") User user);

    @Query("SELECT DISTINCT ja FROM JobApplication ja " +
            "LEFT JOIN FETCH ja.interviews " +
            "WHERE ja.id = :id")
    Optional<JobApplication> findById(@Param("id") Long id);

    List<JobApplication> findByUserAndStatus(User user, JobApplication.ApplicationStatus status);

    List<JobApplication> findByUserAndCompanyNameContainingIgnoreCase(User user, String companyName);

    List<JobApplication> findByUserAndJobTitleContainingIgnoreCase(User user, String jobTitle);

    Long countByUser(User user);

    List<JobApplication> findByUserUsername(String username);
}
