package com.example.invoiceapp.security;

import com.example.invoiceapp.model.SuccessResponse;
import com.example.invoiceapp.model.User; // Your custom User class
import com.example.invoiceapp.service.JwtService;
import com.example.invoiceapp.service.UserService; // Assuming this exists
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtService jwtService;
    private final UserService userService; // Added UserService dependency
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequest creds = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), Collections.emptyList())
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse login request", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        // Extract email from the authenticated principal
        String email = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername();
        
        // Fetch your custom User entity from the database
        User user = userService.getUserByEmail(email); // Ensure this method exists in UserService
        
        // Generate JWT token using the custom User object
        String token = jwtService.generateToken(user);
        
        // Set response content type and write the success response
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(
            new SuccessResponse("Login successful", Collections.singletonMap("token", token))
        ));
    }

    // Inner class for parsing login request body
    private static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}