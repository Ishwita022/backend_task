package com.tka.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = request.getHeader("Authorization");

		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);

			try {
				if (jwtUtil.isTokenValid(token)) {
					String email = jwtUtil.extractEmail(token);
					String role = jwtUtil.extractRole(token);

					// ✅ Ensure role is not null/empty
					if (role != null && !role.isBlank()) {
						// ✅ Add ROLE_ prefix if missing
						if (!role.startsWith("ROLE_")) {
							role = "ROLE_" + role;
						}

						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								email, null, List.of(new SimpleGrantedAuthority(role)));

						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				}

			} catch (JwtException e) {
				// Invalid token → do nothing, request remains unauthenticated
			}
		}

		filterChain.doFilter(request, response);
	}
}