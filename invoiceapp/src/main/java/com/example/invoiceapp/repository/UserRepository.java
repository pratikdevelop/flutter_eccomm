package com.example.invoiceapp.repository;
import java.time.LocalDateTime;
import com.example.invoiceapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailAndResetTokenAndResetTokenExpAfter(String email, String resetToken, LocalDateTime now);
}