package com.example.backend.controllers;


import com.example.backend.DTO.notifications.NotificationRequest;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.handler.annotation.Payload;

@RestController
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    @MessageMapping("/sendTeamTaskFinished/{name}")
//    @SendTo("topic/group-{name}")
    public void sendTeamTaskFinished(@Payload String content, @DestinationVariable String name) {
        String message = content + " is finished!";
        String destination = "/topic/group-" + name;
        System.out.println(destination);
        messagingTemplate.convertAndSend(destination, message);
    }

    @MessageMapping("/sendSessionFinished")
    public void sendSessionFinished(NotificationRequest request) {
        messagingTemplate.convertAndSendToUser(request.getUsername(), "/queue/session-finished", request.getMessage());
    }

    @MessageMapping("/sendSessionPaused")
    public void sendSessionPaused(NotificationRequest request) {
        messagingTemplate.convertAndSendToUser(request.getUsername(), "/queue/session-paused", request.getMessage());
    }

    @MessageMapping("/sendSessionStarted")
    public void sendSessionStarted(NotificationRequest request) {
        messagingTemplate.convertAndSendToUser(request.getUsername(), "/queue/session-started", request.getMessage());
    }






}
