package com.example.backend.controllers;

import com.example.backend.models.Session;
import com.example.backend.models.User;
import com.example.backend.models.enums.SessionType;
import com.example.backend.services.impl.SessionService;
import com.example.backend.services.impl.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@AllArgsConstructor
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    private final UserService userService;


    @Secured("ROLE_USER")
    @PostMapping("/")
    public ResponseEntity<Session> createSession(@RequestBody String sessionType, @AuthenticationPrincipal UserDetails userDetails) {
        SessionType parsedSessionType = SessionType.valueOf(sessionType);

        User user = userService.getUser(userDetails.getUsername());
        Session session = sessionService.create(parsedSessionType, user);
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    @Secured("ROLE_USER")
    @PutMapping("/toggle-pause/{id}")
    public ResponseEntity<Session> toggleSessionPause(@PathVariable Long id, @RequestBody Integer timeLeft) {
        return new ResponseEntity<>(sessionService.togglePause(id,timeLeft), HttpStatus.OK);
    }

    @Secured("ROLE_USER")
    @PutMapping("/finish-session/{id}")
    public ResponseEntity<?> finishSession(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, @RequestBody Long taskId) {
        try{
            User user = userService.getUser(userDetails.getUsername());
            return new ResponseEntity<>(sessionService.finishSession(id, user, taskId), HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
