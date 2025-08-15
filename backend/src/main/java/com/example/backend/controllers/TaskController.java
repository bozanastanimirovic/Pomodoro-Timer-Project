package com.example.backend.controllers;

import com.example.backend.DTO.TaskDTO;
import com.example.backend.models.Task;
import com.example.backend.models.Team;
import com.example.backend.models.User;
import com.example.backend.services.impl.TaskService;
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


import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    private final UserService userService;

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/")
    public List<Task> getAllTask() {
        return taskService.getAll();
    }

    @Secured({"ROLE_USER"})
    @PostMapping("/")
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO taskDTO, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());
        Task task = taskService.create(taskDTO.getTeamName(),taskDTO.getName(), user);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @Secured("ROLE_USER")
    @PutMapping("/name/{id}")
    public ResponseEntity<?> updateTaskName(@PathVariable Long id , @RequestBody String taskName) {
        try{
            return new ResponseEntity<>(taskService.updateName(id, taskName), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Task not found",HttpStatus.NOT_FOUND);
        }
    }

    @Secured("ROLE_USER")
    @PutMapping("/toggle-finished/{id}")
    public ResponseEntity<Task> toggleFinishedTask(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.toggleFinished(id), HttpStatus.OK);
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        try{
            taskService.delete(id);
            return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Task not found",HttpStatus.NOT_FOUND);
        }
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTasksByUserId(@PathVariable Long userId) {
        User user = userService.getById(userId);
        if (user != null) {
            List<Task> tasks = taskService.getByUser(user);
            if (tasks.isEmpty()) {
                return ResponseEntity.status(404).body("Resources with foreign key: " + userId + " do not exist");
            } else {
                return ResponseEntity.ok(tasks);
            }
        }
        return ResponseEntity.status(400).body("Invalid foreign key: " + userId);
    }
}
