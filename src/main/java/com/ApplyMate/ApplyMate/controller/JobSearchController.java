package com.ApplyMate.ApplyMate.controller;

import com.ApplyMate.ApplyMate.entity.JobApplication;
import com.ApplyMate.ApplyMate.service.JobSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/job-search")
public class JobSearchController {

    @Autowired
    private JobSearchService jobSearchService;

    @GetMapping("/search")
    public ResponseEntity<List<JobApplication>> searchApplications(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) JobApplication.ApplicationStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<JobApplication> results = jobSearchService.searchApplications(
                companyName, jobTitle, status, startDate, endDate
        );
        return ResponseEntity.ok(results);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<JobApplication>> getApplicationStats() {
        List<JobApplication> stats = jobSearchService.getApplicationStats();
        return ResponseEntity.ok(stats);
    }
}
