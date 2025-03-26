package com.example.invoiceapp.controller;

import com.example.invoiceapp.model.ErrorResponse;
import com.example.invoiceapp.model.SuccessResponse;
import com.example.invoiceapp.model.User;
import com.example.invoiceapp.service.RecommendationService;
import com.example.invoiceapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userService.getUserProfile(email);
            return ResponseEntity.ok(new SuccessResponse("Profile retrieved successfully", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody User updateData) {
        try {
            String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User updatedUser = userService.updateUserProfile(email, updateData);
            return ResponseEntity.ok(new SuccessResponse("Profile updated successfully", updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("profile_pic") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String filePath = "uploads/" + fileName;
            file.transferTo(new File(filePath));
            return ResponseEntity.ok(new SuccessResponse("File uploaded successfully", Collections.singletonMap("filepath", filePath)));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Error uploading file: " + e.getMessage()));
        }
    }

    @GetMapping("/recommendations")
    public ResponseEntity<?> getRecommendations() {
        try {
            String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userService.getUserProfile(email);
            RecommendationService.Recommendation recs = recommendationService.generateRecommendations(user);
            return ResponseEntity.ok(new SuccessResponse("Recommendations generated successfully", recs));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/admin/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(new SuccessResponse("Users retrieved successfully", users));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}