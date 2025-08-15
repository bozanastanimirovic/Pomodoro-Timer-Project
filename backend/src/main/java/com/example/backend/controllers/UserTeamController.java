package com.example.backend.controllers;

import com.example.backend.DTO.ListDTO;
import com.example.backend.models.Team;
import com.example.backend.models.User;
import com.example.backend.services.impl.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userteams")
@AllArgsConstructor
public class UserTeamController {

    private final TeamService teamService;

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUsersByTeam(@PathVariable Long id) {
        try{
            Team team = teamService.getById(id);
            List<User> members = team.getUsers();
            return ResponseEntity.ok(members);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PostMapping(value = "/addUsers/{id}")
    public ResponseEntity<?> addUsers(@PathVariable Long id, @RequestBody ListDTO requestData){
        try {
            teamService.addUsersToTeam(id, requestData.getItems());
            return ResponseEntity.ok("Users added to team successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/addUser/{id}")
    public ResponseEntity<?> addUser(@PathVariable Long id, @RequestBody Long userId){
        try {
            teamService.addUserToTeam(id, userId);
            return ResponseEntity.ok("User added to team successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, @RequestBody Long userId){
        try {
            teamService.removeUserFromTeam(id, userId);
            return ResponseEntity.ok("User removed from team successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
