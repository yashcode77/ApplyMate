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

    @Transactional(readOnly = true)
    public ResponseEntity<List<JobApplication>> getAllApplications() {
        try {
            User currentUser = getCurrentUser();
            System.out.println("Fetching applications for user: " + currentUser.getUsername());

            List<JobApplication> applications = jobApplicationRepository.findByUser(currentUser);
            System.out.println("Found " + applications.size() + " applications");

            // Debug each application
            applications.forEach(app -> {
                System.out.println("\nApplication ID: " + app.getId());
                System.out.println("Company: " + app.getCompanyName());

                // Debug interviews
                List<Interview> interviews = app.getInterviews();
                System.out.println("Raw interviews object: " + interviews);
                System.out.println("Interviews size: " + (interviews != null ? interviews.size() : "null"));

                if (interviews != null && !interviews.isEmpty()) {
                    interviews.forEach(interview -> {
                        System.out.println("  Interview ID: " + interview.getId());
                        System.out.println("  Round: " + interview.getRoundNumber());
                        System.out.println("  Type: " + interview.getInterviewType());
                        System.out.println("  Status: " + interview.getStatus());
                    });
                }

                // Debug the entire application object
                System.out.println("Full application object: " + app.toString());
            });

            return ResponseEntity.ok(applications);

        } catch (Exception e) {
            System.err.println("Error in getAllApplications: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getApplicationById(Long id) {
        try {
            User currentUser = getCurrentUser();
            JobApplication application = jobApplicationRepository.findById(id)
                    .filter(app -> app.getUser().getId().equals(currentUser.getId()))
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            // Debug log
            System.out.println("Found application: " + application.getId());
            System.out.println("Interviews: " + application.getInterviews().size());

            return ResponseEntity.ok(application);
        } catch (Exception e) {
            System.err.println("Error fetching application: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public ResponseEntity<?> updateApplication(Long id, JobApplicationRequest request) {
        try {
            System.out.println("Updating application: " + id);
            System.out.println("Request data: " + request);

            User currentUser = getCurrentUser();
            JobApplication existingApplication = jobApplicationRepository.findById(id)
                    .filter(app -> app.getUser().getId().equals(currentUser.getId()))
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            // Update basic fields
            existingApplication.setCompanyName(request.getCompanyName());
            existingApplication.setJobTitle(request.getJobTitle());
            existingApplication.setJobDescription(request.getJobDescription());
            existingApplication.setJobUrl(request.getJobUrl());
            existingApplication.setResumeUrl(request.getResumeUrl());
            existingApplication.setStatus(request.getStatus());
            existingApplication.setApplicationDate(request.getApplicationDate());

            // Update interviews
            if (request.getInterviews() != null) {
                System.out.println("Updating " + request.getInterviews().size() + " interviews");

                // Clear existing interviews
                existingApplication.getInterviews().clear();

                // Add updated interviews
                for (InterviewRequest interviewRequest : request.getInterviews()) {
                    System.out.println("Processing interview: " + interviewRequest);

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
            System.out.println("Application updated successfully");

            return ResponseEntity.ok(updatedApplication);
        } catch (Exception e) {
            System.err.println("Error updating application: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
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
