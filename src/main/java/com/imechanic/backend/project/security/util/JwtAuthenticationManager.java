package com.imechanic.backend.project.security.util;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.imechanic.backend.project.exception.JwtAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager {
    private final JwtUtils jwtUtils;

    public DecodedJWT validateToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization");
        if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
            throw new JwtAuthentication("JWT token is missing or invalid");
        }
        jwtToken = jwtToken.substring(7);
        return jwtUtils.validateToken(jwtToken);
    }
    public String getUserRole(DecodedJWT decodedJWT) {
        String role = jwtUtils.getSpecificClaim(decodedJWT, "role").asString();
        return role.substring(5);
    }
}
