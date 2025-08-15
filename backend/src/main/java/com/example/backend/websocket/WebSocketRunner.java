package com.example.backend.websocket;

import com.example.backend.controllers.NotificationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;

@Component
public class WebSocketRunner implements CommandLineRunner {

    @Autowired
    private WebSocketStompClient stompClient;


    @Override
    public void run(String... args) throws Exception {
        StompSessionHandler sessionHandler = new CustomStompSessionHandler();

        StompSession stompSession = stompClient.connect("ws://localhost:8080/notif", sessionHandler).get();

        stompSession.subscribe("/topic/group-tim1", new MyStompFrameHandler());
        String destination2 = "/app/sendTeamTaskFinished/tim1";
        stompSession.send(destination2, "Cao");

        //private
//        String email = "user1@example.com";
//        stompSession.subscribe("/user/" + email + "/queue/notifications", new MyStompFrameHandler());
//        String destination3 = "/app/sendSessionFinished/user1@example.com";
//        stompSession.send(destination3, "Pomodoro");
//
//        String destination4 = "/app/sendSessionFinished/user120@example.com";
//        stompSession.send(destination4, "Pomodoro");



    }

    public static class MyStompFrameHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders headers) {
            System.out.println("Headers " + headers.toString());
            return String.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            if (payload instanceof String) {
                System.out.println("Received: " + payload.toString());
            } else {
                System.out.println("Received unknown payload type");
            }
        }
    }


}


