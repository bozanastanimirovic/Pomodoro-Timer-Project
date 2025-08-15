package com.example.backend.unitTests.services;

import com.example.backend.models.Session;
import com.example.backend.models.Task;
import com.example.backend.models.User;
import com.example.backend.models.enums.SessionType;
import com.example.backend.repositories.SessionRepository;
import com.example.backend.repositories.TaskRepository;
import com.example.backend.services.SessionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionServiceUnitTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private SessionServiceImpl sessionService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("User", "user@example.com", "First", "Last", "password");
        user.setId(1L);
    }

    @Test
    public void testCreateSession_Pomodoro() {
        SessionType sessionType = SessionType.POMODORO;
        Session session = new Session();
        session.setSessionType(sessionType);
        session.setTimeLeft(25 * 60);
        session.setUser(user);
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session result = sessionService.create(sessionType, user);

        assertEquals(session, result);
    }

    @Test
    public void testCreateSession_ShortBreak() {
        SessionType sessionType = SessionType.SHORT_BREAK;
        Session session = new Session();
        session.setSessionType(sessionType);
        session.setTimeLeft(5 * 60);
        session.setUser(user);
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session result = sessionService.create(sessionType, user);

        assertEquals(session, result);
    }

    @Test
    public void testCreateSession_LongBreak() {
        SessionType sessionType = SessionType.LONG_BREAK;
        Session session = new Session();
        session.setSessionType(sessionType);
        session.setTimeLeft(15 * 60);
        session.setUser(user);
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session result = sessionService.create(sessionType, user);

        assertEquals(session, result);
    }

    @Test
    public void testGetUserSessions() {
        Session session = new Session();
        session.setId(1L);
        session.setPaused(false);
        session.setFinished(true);
        session.setTimeLeft(20 * 60);
        session.setUser(user);
        session.setDate(LocalDate.now());

        Session session2 = new Session();
        session2.setId(2L);
        session2.setPaused(false);
        session2.setFinished(true);
        session2.setTimeLeft(20 * 60);
        session2.setUser(user);
        session2.setDate(LocalDate.now());

        List<Session> sessions = Arrays.asList(session, session2);

        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> result = sessionService.getUserSessions(1L);

        assertEquals(sessions, result);
        verify(sessionRepository).findAll();
    }

    @Test
    public void testTogglePause() {
        Session session = new Session();
        session.setId(1L);
        session.setPaused(false);
        session.setTimeLeft(15 * 60);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session result = sessionService.togglePause(1L, 10 * 60);

        assertThat(result.isPaused()).isTrue();
        assertThat(result.getTimeLeft()).isEqualTo(10 * 60);
        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    public void testFinishSession() {
        Session session = new Session();
        session.setId(1L);
        session.setSessionType(SessionType.POMODORO);
        session.setTimeLeft(0);
        session.setUser(user);

        Task task = new Task("Test Task", user, null);
        task.setId(1L);
        task.setSessionsCompleted(0);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session result = sessionService.finishSession(1L, user, 1L);

        assertThat(result.isFinished()).isTrue();
        assertThat(result.getSessionType()).isEqualTo(SessionType.POMODORO);
        assertThat(task.getSessionsCompleted()).isEqualTo(1);
        assertThat(user.getSessionCounter()).isEqualTo(1);
        verify(sessionRepository).save(any(Session.class));
        verify(taskRepository).findById(1L);
    }


}

