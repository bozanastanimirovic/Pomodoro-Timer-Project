package com.example.backend.controllers;

import com.example.backend.models.Session;
import com.example.backend.models.User;
import com.example.backend.models.enums.SessionType;
import com.example.backend.models.enums.UserRole;
import com.example.backend.services.impl.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.services.impl.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	private final SessionService sessionService;

	@Secured("ROLE_ADMIN")
	@GetMapping("/manager")
	public List<User> getAllUsers(){
		return userService.getAll();
	}

	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/manager/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		try{
			User user = userService.getById(id);
			return ResponseEntity.ok(user);
		}catch (Exception e){
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@Secured("ROLE_ADMIN")
	@PutMapping("/manager/{id}")
	public ResponseEntity<?> updateUserManager(@RequestBody String role, @PathVariable Long id){
		UserRole parsedUserRole = UserRole.valueOf(role);
		User updatedUser = userService.updateRole(parsedUserRole, id);
		return ResponseEntity.ok(updatedUser);
	}

	@Secured("ROLE_ADMIN")
	@DeleteMapping("/manager/{id}")
	public ResponseEntity<?> deleteUserManager(@PathVariable Long id){
		if(userService.existsById(id)) {
			userService.delete(id);
			return ResponseEntity.ok("Resource with ID: " + id + " has been successfully deleted");
		}
		return ResponseEntity.status(404).body("Resource with ID: " + id + " couldn't be deleted because it doesn't exist");
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/manager/name")
	public ResponseEntity<?> getUserByName(@RequestBody String name){
		List<User> users = userService.getUsersByName(name);
		if (users.isEmpty()) {
			return ResponseEntity.status(404).body("Resources with this name: " + name + " do not exist");
		}
		return ResponseEntity.ok(users);
	}

	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@GetMapping("/")
	public ResponseEntity<?> getUser(@AuthenticationPrincipal UserDetails userDetails){
		return ResponseEntity.ok(userService.getUser(userDetails.getUsername()));
	}

	@Secured("ROLE_USER")
	@DeleteMapping("/")
	public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetails userDetails){
		User user = userService.getUser(userDetails.getUsername());
		userService.delete(user.getId());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Secured("ROLE_USER")
	@GetMapping("/statistics")
	public ResponseEntity<List<Session>> userStatistics(@AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.getUser(userDetails.getUsername());
		List<Session> sessions = sessionService.getUserSessions(user.getId());
		return new ResponseEntity<>(sessions, HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/allUsersStatistics")
	public ResponseEntity<List<Session>> usersStatistics() {
		List<Session> sessions = sessionService.getUsersSessions();
		return new ResponseEntity<>(sessions, HttpStatus.OK);
	}

}

