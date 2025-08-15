package com.example.backend.services.impl;

import com.example.backend.models.Session;
import com.example.backend.models.Task;
import com.example.backend.models.User;
import com.example.backend.models.enums.SessionType;

import java.util.List;

public interface SessionService {
    Session create(SessionType sessionType, User user);

    void update(boolean isFinished, boolean isPaused, int timeLeft, Long id);

    List<Session> getUserSessions(Long userId);

    List<Session> getUsersSessions();

    Session togglePause(Long id, Integer timeLeft);

    Session finishSession(Long id, User user, Long taskId);

}
