package com.ApplyMate.ApplyMate.controller;

import com.ApplyMate.ApplyMate.entity.JobApplication;
import jakarta.validation.constraints.NotBlank;

public class JobApplicationRequest {
    @NotBlank
    private String companyName;

    @NotBlank
    private String jobTitle;

    private String jobDescription;
    private String jobUrl;
    private JobApplication.ApplicationStatus status;

    // Constructors
    public JobApplicationRequest() {}

    public JobApplicationRequest(String companyName, String jobTitle, String jobDescription, String jobUrl) {
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.jobUrl = jobUrl;
    }

    // Getters and Setters
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getJobDescription() { return jobDescription; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }

    public String getJobUrl() { return jobUrl; }
    public void setJobUrl(String jobUrl) { this.jobUrl = jobUrl; }

    public JobApplication.ApplicationStatus getStatus() { return status; }
    public void setStatus(JobApplication.ApplicationStatus status) { this.status = status; }
}