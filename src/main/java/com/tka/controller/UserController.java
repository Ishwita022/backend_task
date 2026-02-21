package com.tka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.tka.entity.User;
import com.tka.repository.UserRepository;
import com.tka.security.JwtUtil;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private JwtUtil jwtUtil;

	// =========================
	// Normal user registration
	// =========================
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {

		System.out.println("==== DEBUG START ====");
		System.out.println("Incoming user object: " + user);
		System.out.println("Incoming role: " + user.getRole());

		if (user.getRole() == null || user.getRole().isEmpty()) {
			user.setRole("ROLE_USER");
			System.out.println("Role was null → set to ROLE_USER");
		} else if (!user.getRole().startsWith("ROLE_")) {
			user.setRole("ROLE_" + user.getRole().toUpperCase());
			System.out.println("Role converted to: " + user.getRole());
		}

		System.out.println("Final role before save: " + user.getRole());
		System.out.println("==== DEBUG END ====");

		user.setPassword(encoder.encode(user.getPassword()));
		userRepo.save(user);
		return ResponseEntity.ok("User registered successfully");
	}

	// =========================
	// Admin registration (protected)
	// =========================
	@PostMapping("/admin/register")
	@PreAuthorize("hasRole('ADMIN')") // Only logged-in admins
	public ResponseEntity<?> registerAdmin(@RequestBody User user) {

		user.setRole("ROLE_ADMIN");
		user.setPassword(encoder.encode(user.getPassword()));
		userRepo.save(user);

		return ResponseEntity.ok("Admin registered successfully");
	}

	// =========================
	// Login endpoint
	// =========================
	@PostMapping("/login")
	public String login(@RequestBody User loginUser) {

		User dbUser = userRepo.findByEmail(loginUser.getEmail());

		if (dbUser == null)
			return "User not found";

		if (!encoder.matches(loginUser.getPassword(), dbUser.getPassword()))
			return "Invalid password";

		String token = jwtUtil.generateToken(dbUser.getEmail(), dbUser.getRole());

		return token;
	}

	// =========================
	// Protected dashboard
	// =========================
	@GetMapping("/dashboard")
	public String dashboard() {
		return "Hello, this is protected data";
	}
}