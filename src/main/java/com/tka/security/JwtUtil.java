package com.tka.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

	// Must be 32+ characters for HS256
	private static final String SECRET = "MyUltraSecureSecretKeyForJwt123456";

	private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

	// Generate token
	public String generateToken(String email, String role) {

		if (!role.startsWith("ROLE_")) {
			role = "ROLE_" + role;
		}

		return Jwts.builder().setSubject(email).claim("role", role).setIssuer("TKA").setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	// Extract email (subject)
	public String extractEmail(String token) {
		return extractClaims(token).getSubject();
	}

	// Extract role
	public String extractRole(String token) {
		return extractClaims(token).get("role", String.class);
	}

	// Validate token
	public boolean isTokenValid(String token) {
		try {
			extractClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	// Parse claims safely
	private Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

}