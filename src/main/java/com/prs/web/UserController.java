package com.prs.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.JsonResponse;
import com.prs.business.User;
import com.prs.db.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserRepository userRepo;

	@GetMapping("/")
	public JsonResponse list() {
		JsonResponse jr = null;
		List<User> users = userRepo.findAll();
		if (users.size() > 0) {
			jr = JsonResponse.getInstance(users);
		} else {
			jr = JsonResponse.getErrorInstance("No users found.");
		}
		return jr;
	}

//login method
	@PostMapping("/login")
	public JsonResponse login(@RequestBody User u) {
		JsonResponse jr = null;
		Optional<User> user = userRepo.findByUserNameAndPassword(u.getUserName(), u.getPassword());
		if (user.isPresent()) {
			jr = JsonResponse.getInstance(user.get());
		} else {
			jr = JsonResponse.getErrorInstance("Invalid username/pwd combination try again");
		}

		return jr;
	}

// get method
	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<User> user = userRepo.findById(id);
		if (user.isPresent()) {
			jr = JsonResponse.getInstance(user.get());
		} else {
			jr = JsonResponse.getErrorInstance("No user found for ID: " + id);
		}

		return jr;
	}

// 'create' method
	@PostMapping("/")
	public JsonResponse createUser(@RequestBody User user) {
		JsonResponse jr = null;

		try {
			user = userRepo.save(user);
			jr = JsonResponse.getInstance(user);
		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getErrorInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Error creating user: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}

// update method
	@PutMapping("/")
	public JsonResponse updateUser(@RequestBody User user) {
		JsonResponse jr = null;

		try {
			user = userRepo.save(user);
			jr = JsonResponse.getInstance(user);
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Error updating user: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}

//delete method
	@DeleteMapping("/{id}")
	public JsonResponse deleteUser(@PathVariable int id) {
		JsonResponse jr = null;

		try {
			userRepo.deleteById(id);
			jr = JsonResponse.getInstance(id);
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Error deleting user: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}
}
