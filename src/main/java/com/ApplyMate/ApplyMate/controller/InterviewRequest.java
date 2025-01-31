package com.ApplyMate.ApplyMate.controller;

import java.time.LocalDateTime;

public class InterviewRequest {
    private Long id;
    private Long jobApplicationId;
    private Integer roundNumber;
    private LocalDateTime interviewDate;
    private String interviewType;
    private String notes;
    private String status;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getJobApplicationId() {
        return jobApplicationId;
    }
    public void setJobApplicationId(Long jobApplicationId) {
        this.jobApplicationId = jobApplicationId;
    }
    public Integer getRoundNumber() {
        return roundNumber;
    }
    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }
    public LocalDateTime getInterviewDate() {
        return interviewDate;
    }
    public void setInterviewDate(LocalDateTime interviewDate) {
        this.interviewDate = interviewDate;
    }
    public String getInterviewType() {
        return interviewType;
    }
    public void setInterviewType(String interviewType) {
        this.interviewType = interviewType;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    // Getters and setters
    
}
