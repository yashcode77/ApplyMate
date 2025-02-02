package com.ApplyMate.ApplyMate.service;

import com.ApplyMate.ApplyMate.controller.InterviewRequest;
import com.ApplyMate.ApplyMate.controller.JobApplicationRequest;
import com.ApplyMate.ApplyMate.entity.JobApplication;
import com.ApplyMate.ApplyMate.entity.User;
import com.ApplyMate.ApplyMate.entity.Interview;
import com.ApplyMate.ApplyMate.repository.JobApplicationRepository;
import com.ApplyMate.ApplyMate.repository.UserRepository;
import com.ApplyMate.ApplyMate.repository.InterviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public ResponseEntity<?> createApplication(JobApplicationRequest request) {
        try {
            System.out.println("Starting job application creation...");
            User currentUser = getCurrentUser();

            JobApplication application = new JobApplication();
            application.setUser(currentUser);
            application.setCompanyName(request.getCompanyName());
            application.setJobTitle(request.getJobTitle());
            application.setJobDescription(request.getJobDescription());
            application.setJobUrl(request.getJobUrl());
            application.setResumeUrl(request.getResumeUrl());
            application.setStatus(request.getStatus());
            application.setApplicationDate(request.getApplicationDate());

            System.out.println("Saving job application...");
            JobApplication savedApplication = jobApplicationRepository.save(application);
            System.out.println("Job application saved with ID: " + savedApplication.getId());

            if (request.getInterviews() != null && !request.getInterviews().isEmpty()) {
                System.out.println("Found " + request.getInterviews().size() + " interviews to process");

                for (InterviewRequest interviewRequest : request.getInterviews()) {
                    System.out.println("Creating interview with round number: " + interviewRequest.getRoundNumber());

                    Interview interview = new Interview();
                    interview.setJobApplication(savedApplication);
                    interview.setRoundNumber(interviewRequest.getRoundNumber());
                    interview.setInterviewDate(interviewRequest.getInterviewDate());
                    interview.setInterviewType(interviewRequest.getInterviewType());
                    interview.setNotes(interviewRequest.getNotes());
                    interview.setStatus(interviewRequest.getStatus());

                    savedApplication.getInterviews().add(interview);
                    System.out.println("Interview added to application's interview list");
                }

                System.out.println("Saving application with interviews...");
                jobApplicationRepository.save(savedApplication);
                System.out.println("Application saved with interviews");
            } else {
                System.out.println("No interviews to process");
            }

            return ResponseEntity.ok(savedApplication);
        } catch (Exception e) {
            System.err.println("Error creating application: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
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

    @Transactional
    public ResponseEntity<?> updateApplication(Long id, JobApplicationRequest request) {
        User currentUser = getCurrentUser();
        JobApplication existingApplication = jobApplicationRepository.findById(id)
                .filter(app -> app.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new RuntimeException("Application not found"));

        existingApplication.setCompanyName(request.getCompanyName());
        existingApplication.setJobTitle(request.getJobTitle());
        existingApplication.setJobDescription(request.getJobDescription());
        existingApplication.setJobUrl(request.getJobUrl());
        existingApplication.setResumeUrl(request.getResumeUrl());
        existingApplication.setStatus(request.getStatus());
        existingApplication.setApplicationDate(request.getApplicationDate());

        // Update interviews
        if (request.getInterviews() != null) {
            // Remove existing interviews
            existingApplication.getInterviews().clear();

            // Add new interviews
            for (InterviewRequest interviewRequest : request.getInterviews()) {
                Interview interview = new Interview();
                interview.setJobApplication(existingApplication);
                interview.setRoundNumber(interviewRequest.getRoundNumber());
                interview.setInterviewDate(interviewRequest.getInterviewDate());
                interview.setInterviewType(interviewRequest.getInterviewType());
                interview.setNotes(interviewRequest.getNotes());
                interview.setStatus(interviewRequest.getStatus());

                existingApplication.getInterviews().add(interview);
            }
        }

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

    public List<JobApplication> getAllApplicationsByUsername(String username) {
        return jobApplicationRepository.findByUserUsername(username);
    }
}
