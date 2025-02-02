package com.ApplyMate.ApplyMate.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ApplyMate.ApplyMate.entity.Interview;
import com.ApplyMate.ApplyMate.service.InterviewService;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {
    @Autowired
    private InterviewService interviewService;

    @PostMapping
    public ResponseEntity<InterviewResponse> createInterview(@RequestBody InterviewRequest request) {
        Interview interview = interviewService.createInterview(request);
        return ResponseEntity.ok(new InterviewResponse(interview));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterviewResponse> updateInterview(
            @PathVariable Long id,
            @RequestBody InterviewRequest request) {
        Interview interview = interviewService.updateInterview(id, request);
        return ResponseEntity.ok(new InterviewResponse(interview));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterview(@PathVariable Long id) {
        interviewService.deleteInterview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<Interview>> getInterviewsByApplication(@PathVariable Long applicationId) {
        System.out.println("Fetching interviews for application ID: " + applicationId);
        List<Interview> interviews = interviewService.getInterviewsByApplication(applicationId);
        return ResponseEntity.ok(interviews);
    }
}