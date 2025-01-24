package com.ApplyMate.ApplyMate.repository;

import com.ApplyMate.ApplyMate.entity.JobApplication;
import com.ApplyMate.ApplyMate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByUser(User user);
    List<JobApplication> findByUserAndStatus(User user, JobApplication.ApplicationStatus status);
    Long countByUser(User user);
}
