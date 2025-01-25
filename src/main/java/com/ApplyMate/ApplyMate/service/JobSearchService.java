package com.ApplyMate.ApplyMate.service;

import com.ApplyMate.ApplyMate.entity.JobApplication;
import com.ApplyMate.ApplyMate.repository.JobApplicationRepository;
import com.ApplyMate.ApplyMate.repository.UserRepository;
import com.ApplyMate.ApplyMate.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobSearchService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<JobApplication> searchApplications(
            String companyName,
            String jobTitle,
            JobApplication.ApplicationStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        User currentUser = getCurrentUser();

        return jobApplicationRepository.findAll(
                Specification.where(
                        (root, query, criteriaBuilder) -> {
                            List<Predicate> predicates = new ArrayList<>();

                            // User specific
                            predicates.add(criteriaBuilder.equal(root.get("user"), currentUser));

                            // Company Name filter
                            if (companyName != null && !companyName.isEmpty()) {
                                predicates.add(criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("companyName")),
                                        "%" + companyName.toLowerCase() + "%"
                                ));
                            }

                            // Job Title filter
                            if (jobTitle != null && !jobTitle.isEmpty()) {
                                predicates.add(criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("jobTitle")),
                                        "%" + jobTitle.toLowerCase() + "%"
                                ));
                            }

                            // Status filter
                            if (status != null) {
                                predicates.add(criteriaBuilder.equal(root.get("status"), status));
                            }

                            // Date range filter
                            if (startDate != null && endDate != null) {
                                predicates.add(criteriaBuilder.between(
                                        root.get("applicationDate"),
                                        startDate,
                                        endDate
                                ));
                            }

                            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                        }
                )
        );
    }

    public List<JobApplication> getApplicationStats() {
        User currentUser = getCurrentUser();
        return jobApplicationRepository.findByUser(currentUser);
    }
}
