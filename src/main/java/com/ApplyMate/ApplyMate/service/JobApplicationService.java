package com.ApplyMate.ApplyMate.service;

import com.ApplyMate.ApplyMate.controller.JobApplicationRequest;
import com.ApplyMate.ApplyMate.entity.JobApplication;
import com.ApplyMate.ApplyMate.entity.User;
import com.ApplyMate.ApplyMate.repository.JobApplicationRepository;
import com.ApplyMate.ApplyMate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public ResponseEntity<?> createApplication(JobApplicationRequest request) {
        User currentUser = getCurrentUser();

        JobApplication application = new JobApplication();
        application.setUser(currentUser);
        application.setCompanyName(request.getCompanyName());
        application.setJobTitle(request.getJobTitle());
        application.setJobDescription(request.getJobDescription());
        application.setJobUrl(request.getJobUrl());
        application.setStatus(request.getStatus());

        JobApplication savedApplication = jobApplicationRepository.save(application);
        return ResponseEntity.ok(savedApplication);
    }

    public ResponseEntity<List<JobApplication>> getAllApplications() {
        User currentUser = getCurrentUser();
        List<JobApplication> applications = jobApplicationRepository.findByUser(currentUser);
        return ResponseEntity.ok(applications);
    }

    public ResponseEntity<?> getApplicationById(Long id) {
        User currentUser = getCurrentUser();
        JobApplication application = jobApplicationRepository.findById(id)
                .filter(app -> app.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new RuntimeException("Application not found"));

        return ResponseEntity.ok(application);
    }

    public ResponseEntity<?> updateApplication(Long id, JobApplicationRequest request) {
        User currentUser = getCurrentUser();
        JobApplication existingApplication = jobApplicationRepository.findById(id)
                .filter(app -> app.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new RuntimeException("Application not found"));

        existingApplication.setCompanyName(request.getCompanyName());
        existingApplication.setJobTitle(request.getJobTitle());
        existingApplication.setJobDescription(request.getJobDescription());
        existingApplication.setJobUrl(request.getJobUrl());
        existingApplication.setStatus(request.getStatus());

        JobApplication updatedApplication = jobApplicationRepository.save(existingApplication);
        return ResponseEntity.ok(updatedApplication);
    }

    public ResponseEntity<?> deleteApplication(Long id) {
        User currentUser = getCurrentUser();
        JobApplication application = jobApplicationRepository.findById(id)
                .filter(app -> app.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new RuntimeException("Application not found"));

        jobApplicationRepository.delete(application);
        return ResponseEntity.ok("Application deleted successfully");
    }
}
