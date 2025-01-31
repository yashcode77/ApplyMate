package com.ApplyMate.ApplyMate.service;

import com.ApplyMate.ApplyMate.controller.InterviewRequest;
import com.ApplyMate.ApplyMate.entity.Interview;
import com.ApplyMate.ApplyMate.entity.JobApplication;
import com.ApplyMate.ApplyMate.repository.InterviewRepository;
import com.ApplyMate.ApplyMate.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class InterviewService {

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    /**
     * Creates a new interview for a job application
     */
    @Transactional
    public Interview createInterview(InterviewRequest request) {
        // Verify the job application exists
        JobApplication jobApplication = jobApplicationRepository.findById(request.getJobApplicationId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Job Application not found with id: " + request.getJobApplicationId()));

        // Validate round number
        validateRoundNumber(request.getRoundNumber(), request.getJobApplicationId());

        // Create and populate new interview
        Interview interview = new Interview();
        updateInterviewFromRequest(interview, request);
        interview.setJobApplication(jobApplication);
        
        return interviewRepository.save(interview);
    }

    /**
     * Updates an existing interview
     */
    @Transactional
    public Interview updateInterview(Long id, InterviewRequest request) {
        Interview interview = interviewRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Interview not found with id: " + id));

        // If round number is being changed, validate it
        if (!interview.getRoundNumber().equals(request.getRoundNumber())) {
            validateRoundNumber(request.getRoundNumber(), request.getJobApplicationId());
        }

        updateInterviewFromRequest(interview, request);
        return interviewRepository.save(interview);
    }

    /**
     * Deletes an interview
     */
    @Transactional
    public void deleteInterview(Long id) {
        Interview interview = interviewRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Interview not found with id: " + id));
        
        // Reorder remaining rounds if necessary
        reorderRoundsAfterDeletion(interview);
        
        interviewRepository.deleteById(id);
    }

    /**
     * Retrieves all interviews for a specific job application
     */
    public List<Interview> getInterviewsByApplication(Long applicationId) {
        // Verify the job application exists
        if (!jobApplicationRepository.existsById(applicationId)) {
            throw new EntityNotFoundException("Job Application not found with id: " + applicationId);
        }
        return interviewRepository.findByJobApplicationIdOrderByInterviewDateAsc(applicationId);
    }

    /**
     * Gets a specific interview by ID
     */
    public Interview getInterviewById(Long id) {
        return interviewRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Interview not found with id: " + id));
    }

    /**
     * Updates interview status
     */
    @Transactional
    public Interview updateInterviewStatus(Long id, String status) {
        Interview interview = getInterviewById(id);
        interview.setStatus(status);
        return interviewRepository.save(interview);
    }

    /**
     * Helper method to update interview fields from request
     */
    private void updateInterviewFromRequest(Interview interview, InterviewRequest request) {
        interview.setRoundNumber(request.getRoundNumber());
        interview.setInterviewDate(request.getInterviewDate());
        interview.setInterviewType(request.getInterviewType());
        interview.setNotes(request.getNotes());
        interview.setStatus(request.getStatus());
    }

    /**
     * Validates that the round number is unique for the job application
     */
    private void validateRoundNumber(Integer roundNumber, Long jobApplicationId) {
        Optional<Interview> existingInterview = interviewRepository.findByJobApplicationIdAndRoundNumber(
            jobApplicationId, roundNumber);
        
        if (existingInterview.isPresent()) {
            throw new IllegalStateException(
                "An interview with round number " + roundNumber + 
                " already exists for this application");
        }
    }

    /**
     * Reorders remaining rounds after an interview is deleted
     */
    private void reorderRoundsAfterDeletion(Interview deletedInterview) {
        List<Interview> laterRounds = interviewRepository
            .findByJobApplicationIdAndRoundNumberGreaterThan(
                deletedInterview.getJobApplication().getId(),
                deletedInterview.getRoundNumber());

        for (Interview interview : laterRounds) {
            interview.setRoundNumber(interview.getRoundNumber() - 1);
            interviewRepository.save(interview);
        }
    }

    /**
     * Gets the next available round number for a job application
     */
    public Integer getNextRoundNumber(Long jobApplicationId) {
        return interviewRepository.findMaxRoundNumberByJobApplicationId(jobApplicationId)
            .map(maxRound -> maxRound + 1)
            .orElse(1);
    }

    /**
     * Bulk updates interview statuses
     */
    @Transactional
    public void bulkUpdateStatus(List<Long> interviewIds, String newStatus) {
        for (Long id : interviewIds) {
            Interview interview = getInterviewById(id);
            interview.setStatus(newStatus);
            interviewRepository.save(interview);
        }
    }
}