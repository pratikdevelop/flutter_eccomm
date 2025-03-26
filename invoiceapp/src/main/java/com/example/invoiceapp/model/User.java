package com.example.invoiceapp.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String username;
    private String password;
    private String profilePic;
    private String phone;
    private LocalDateTime dateOfBirth;
    private String gender;
    private String address;
    private String city;
    private String state;
    private String country;
    private String pinCode;
    private String role = "user";
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isActive = true;
    private Map<String, String> preferences;
    private double behaviorScore;
    private LocalDateTime lastPrediction;
    private String resetToken;
    private LocalDateTime resetTokenExp;
}