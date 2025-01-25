package com.ApplyMate.ApplyMate.service;


import com.ApplyMate.ApplyMate.entity.User;
import com.ApplyMate.ApplyMate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadService {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private UserRepository userRepository;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String uploadResume(MultipartFile file) throws IOException {
        User currentUser = getCurrentUser();
        String fileName = generateUniqueFileName(currentUser.getUsername(), file.getOriginalFilename());

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return fileName;
    }

    private String generateUniqueFileName(String username, String originalFileName) {
        return "resumes/" + username + "/" + UUID.randomUUID() + "_" + originalFileName;
    }
}
