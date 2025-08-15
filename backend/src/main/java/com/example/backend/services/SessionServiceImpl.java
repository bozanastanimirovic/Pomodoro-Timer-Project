package com.example.backend.services;
import com.example.backend.models.Session;
import com.example.backend.models.Task;
import com.example.backend.models.User;
import com.example.backend.models.enums.SessionType;
import com.example.backend.repositories.SessionRepository;
import com.example.backend.repositories.TaskRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.impl.SessionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public Session create(SessionType sessionType, User user) {
        Session session = new Session();
        session.setDate(LocalDate.now());
        session.setPaused(false);
        session.setFinished(false);
        session.setSessionType(sessionType);
        if (sessionType.equals(SessionType.POMODORO)) {
            session.setTimeLeft(25*60);
        } else if (sessionType.equals(SessionType.SHORT_BREAK)) {
            session.setTimeLeft(5*60);
        } else {
            session.setTimeLeft(15*60);
        }
        session.setUser(user);
        return sessionRepository.save(session);
    }

    @Override
    public void update(boolean isFinished, boolean isPaused, int timeLeft, Long id) {
        sessionRepository.findById(id).map((session -> {
            session.setPaused(isPaused);
            session.setFinished(isFinished);
            session.setTimeLeft(timeLeft);
            return sessionRepository.save(session);
        })).orElseThrow(() -> new RuntimeException("Error: Session not found"));
    }

    @Override
    public List<Session> getUserSessions(Long userId) {
        List<Session> sessions = new ArrayList<>();
        for (Session session: sessionRepository.findAll()) {
            if (session.getUser().getId().equals(userId) && session.isFinished()) {
                sessions.add(session);
            }
        }
        return sessions;
    }

    @Override
    public List<Session> getUsersSessions() {
        List<Session> sessions = new ArrayList<>();
        for (Session session: sessionRepository.findAll()) {
            if (session.isFinished()) {
                sessions.add(session);
            }
        }
        return sessions;
    }

    @Override
    public Session togglePause(Long id, Integer timeLeft) {
        Session session= sessionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Session not found"));
        session.setTimeLeft(timeLeft);
        session.setPaused(!session.isPaused());
        return sessionRepository.save(session);
    }

    @Override
    public Session finishSession(Long id, User user, Long taskId) {
        Session session = sessionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Session not found"));
        session.setFinished(true);
        session.setTimeLeft(0);
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if(session.getSessionType().equals(SessionType.POMODORO)){
            if(session.getTimeLeft() == 0){
                user.setSessionCounter(user.getSessionCounter()+1);
                task.setSessionsCompleted(task.getSessionsCompleted()+1);
                taskRepository.save(task);
                userRepository.save(user);
            }
        }
        return sessionRepository.save(session);
    }
}
