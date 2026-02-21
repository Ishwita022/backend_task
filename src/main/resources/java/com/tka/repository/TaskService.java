package com.tka.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tka.entity.Task;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepo;

	public Task createTask(Task task, String email) {
		task.setCreatedBy(email);
		return taskRepo.save(task);
	}

	public List<Task> getUserTasks(String email) {

		var auth = SecurityContextHolder.getContext().getAuthentication();
		boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

		if (isAdmin) {
			return taskRepo.findAll(); // ✅ Admin sees all tasks
		}
		return taskRepo.findByCreatedBy(email);
	}

	public Task updateTask(Long id, Task updatedTask) {
		Task task = taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

		task.setTitle(updatedTask.getTitle());
		task.setDescription(updatedTask.getDescription());
		task.setCompleted(updatedTask.isCompleted());

		return taskRepo.save(task);
	}

	public void deleteTask(Long id, String username, String role) {
		Task task = taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

		// If USER → can delete only own task
		if (role.equals("ROLE_USER") && !task.getCreatedBy().equals(username)) {
			throw new RuntimeException("Not authorized");
		}

		taskRepo.delete(task);
	}
}