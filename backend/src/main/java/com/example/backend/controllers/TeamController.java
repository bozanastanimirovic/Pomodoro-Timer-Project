package com.example.backend.controllers;

import java.net.URI;
import java.util.List;

import com.example.backend.services.impl.TeamService;
import com.example.backend.services.impl.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.example.backend.models.Team;

@RestController
@AllArgsConstructor
@Secured("ROLE_ADMIN")
@RequestMapping("/api/teams")
public class TeamController {

	private final TeamService teamService;

	private final UserService userService;

	
	@GetMapping("/")
	public List<Team> getAllTeams(){
		return teamService.getAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getTeamById(@PathVariable Long id) {
		return new ResponseEntity<>(teamService.getById(id), HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<?> createTeam(@RequestBody String teamName){
		if(teamService.existsByTeamName(teamName)) {
			return ResponseEntity.status(409).body("Team name already exists");
		}
		Team team = new Team(teamName);
		Team savedTeam = teamService.create(team);
		URI uri = URI.create("/team/" + savedTeam.getId());
		return ResponseEntity.created(uri).body(savedTeam);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateTeamName(@PathVariable Long id,@RequestBody String teamName){
		try{
			Team updatedTeam = teamService.updateName(teamName, id);
			return ResponseEntity.ok(updatedTeam);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteTeam(@PathVariable Long id){
		if(teamService.existsById(id)) {
			teamService.delete(id);
			return ResponseEntity.ok("Resource with ID: " + id + " has been successfully deleted");
		}
		return ResponseEntity.status(404).body("Resource with ID: " + id + " couldn't be deleted because it doesn't exist");
	}

	@Secured("ROLE_USER")
	@GetMapping("/user")
	public List<Team> getAllTeamsUser(@AuthenticationPrincipal UserDetails userDetails) {
		return userService.getUser(userDetails.getUsername()).getTeams();
	}
}
