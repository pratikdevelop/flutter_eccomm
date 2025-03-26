package com.example.invoiceapp.service;

import com.example.invoiceapp.model.User;
import com.example.invoiceapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.invoiceapp.model.User;
import com.example.invoiceapp.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent() ||
            userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Email or username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRole("user");
        user.setActive(true);
        return userRepository.save(user);
    }

    public String loginUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return jwtService.generateToken(userOpt.get());
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExp(LocalDateTime.now().plusHours(1));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // In production, send email with resetToken
    }

    public void resetPassword(String email, String resetToken, String newPassword) {
        User user = userRepository.findByEmailAndResetTokenAndResetTokenExpAfter(email, resetToken, LocalDateTime.now())
            .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExp(null);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public User getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(null); // Hide password
        return user;
    }

    public User updateUserProfile(String email, User updateData) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (updateData.getName() != null) user.setName(updateData.getName());
        if (updateData.getUsername() != null) user.setUsername(updateData.getUsername());
        if (updateData.getProfilePic() != null) user.setProfilePic(updateData.getProfilePic());
        if (updateData.getPhone() != null) user.setPhone(updateData.getPhone());
        if (updateData.getDateOfBirth() != null) user.setDateOfBirth(updateData.getDateOfBirth());
        if (updateData.getGender() != null) user.setGender(updateData.getGender());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
            .filter(User::isActive)
            .peek(user -> user.setPassword(null))
            .collect(Collectors.toList());
    }
    public User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"));
}
}