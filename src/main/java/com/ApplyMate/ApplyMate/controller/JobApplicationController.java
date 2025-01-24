package com.ApplyMate.ApplyMate.controller;

import com.ApplyMate.ApplyMate.controller.JobApplicationRequest;
import com.ApplyMate.ApplyMate.entity.JobApplication;
import com.ApplyMate.ApplyMate.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    @Autowired
    private JobApplicationService jobApplicationService;

    @PostMapping
    public ResponseEntity<?> createApplication(@Valid @RequestBody JobApplicationRequest request) {
        return jobApplicationService.createApplication(request);
    }

    @GetMapping
    public ResponseEntity<List<JobApplication>> getAllApplications() {
        return jobApplicationService.getAllApplications();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getApplicationById(@PathVariable Long id) {
        return jobApplicationService.getApplicationById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationRequest request
    ) {
        return jobApplicationService.updateApplication(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id) {
        return jobApplicationService.deleteApplication(id);
    }
}
