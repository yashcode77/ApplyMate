package com.ApplyMate.ApplyMate.controller;

import com.ApplyMate.ApplyMate.controller.JobApplicationRequest;
import com.ApplyMate.ApplyMate.entity.JobApplication;
import com.ApplyMate.ApplyMate.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

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
    public ResponseEntity<?> getAllApplications(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            String username = userDetails.getUsername();
            List<JobApplication> applications = jobApplicationService.getAllApplicationsByUsername(username);
            
            // Debug log
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(applications);
            System.out.println("Response JSON: " + jsonString);
            
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching applications: " + e.getMessage());
        }
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
