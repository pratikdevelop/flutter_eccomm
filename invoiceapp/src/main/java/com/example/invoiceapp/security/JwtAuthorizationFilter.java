package com.example.invoiceapp.security;

import com.example.invoiceapp.service.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthorizationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        
        // Check if the Authorization header is present and starts with "Bearer "
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Extract token and validate it
        String token = header.substring(7); // Remove "Bearer " prefix
        try {
            Claims claims = jwtService.validateToken(token);
            String email = (String) claims.get("email");
            String role = (String) claims.get("role");

            // Create authentication object with the user's email and role
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                email, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
            );
            
            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            // If token validation fails, return 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Proceed with the filter chain
        chain.doFilter(request, response);
    }
}