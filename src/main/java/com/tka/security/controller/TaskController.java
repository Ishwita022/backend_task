package com.tka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tka.entity.Task;
import com.tka.repository.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	@Autowired
	private TaskService taskService;

	@PostMapping
	public Task create(@RequestBody Task task) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return taskService.createTask(task, email);
	}

	@GetMapping
	public List<Task> getMyTasks() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return taskService.getUserTasks(email);
	}

	@PutMapping("/{id}")
	public Task update(@PathVariable Long id, @RequestBody Task task) {
		return taskService.updateTask(id, task);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteTask(@PathVariable Long id, Authentication authentication) {

		String username = authentication.getName();
		String role = authentication.getAuthorities().iterator().next().getAuthority();
		taskService.deleteTask(id, username, role);
		return ResponseEntity.ok("Deleted by " + username);
	}
}
