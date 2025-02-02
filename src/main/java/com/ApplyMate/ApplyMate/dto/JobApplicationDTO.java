package com.ApplyMate.ApplyMate.dto;

import com.ApplyMate.ApplyMate.entity.JobApplication;
import java.time.LocalDateTime;

public class JobApplicationDTO {
    private Long id;
    private String companyName;
    private String jobTitle;
    private JobApplication.ApplicationStatus status;
    private String jobDescription;
    private String jobUrl;
    private LocalDateTime applicationDate;
    private LocalDateTime lastUpdated;
    private String resumeUrl;

    public JobApplicationDTO(JobApplication application) {
        this.id = application.getId();
        this.companyName = application.getCompanyName();
        this.jobTitle = application.getJobTitle();
        this.status = application.getStatus();
        this.jobDescription = application.getJobDescription();
        this.jobUrl = application.getJobUrl();
        this.applicationDate = application.getApplicationDate();
        this.lastUpdated = application.getLastUpdated();
        this.resumeUrl = application.getResumeUrl();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public JobApplication.ApplicationStatus getStatus() { return status; }
    public void setStatus(JobApplication.ApplicationStatus status) { this.status = status; }

    public String getJobDescription() { return jobDescription; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }

    public String getJobUrl() { return jobUrl; }
    public void setJobUrl(String jobUrl) { this.jobUrl = jobUrl; }

    public LocalDateTime getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDateTime applicationDate) { this.applicationDate = applicationDate; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
} 