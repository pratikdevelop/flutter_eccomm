package com.example.invoiceapp.controller; // Correct package (singular "controller")

import com.example.invoiceapp.model.ErrorResponse;
import com.example.invoiceapp.model.SuccessResponse;
import com.example.invoiceapp.model.User;
import com.example.invoiceapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User savedUser = userService.registerUser(user);
            String token = userService.loginUser(user.getEmail(), user.getPassword()); // Assumes loginUser returns token
            return ResponseEntity.status(201)
                .body(new SuccessResponse("Registration successful", Collections.singletonMap("token", token)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            userService.forgotPassword(request.get("email"));
            return ResponseEntity.ok(new SuccessResponse("Reset token generated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            userService.resetPassword(request.get("email"), request.get("resetToken"), request.get("newPassword"));
            return ResponseEntity.ok(new SuccessResponse("Password reset successful"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}