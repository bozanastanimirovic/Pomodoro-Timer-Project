package com.example.backend.unitTests.services;

import com.example.backend.models.Task;
import com.example.backend.models.Team;
import com.example.backend.models.User;
import com.example.backend.repositories.TaskRepository;
import com.example.backend.repositories.TeamRepository;
import com.example.backend.services.TaskServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceUnitTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private User user;
    private Team team;

    @BeforeEach
    void setUp() {
        user = new User("User1", "user@example.com", "Name", "Surname", "password");
        team = new Team("Team1");
    }

    @Test
    public void testGetAll() {
        List<Task> tasks = Arrays.asList(new Task("Task1", user, team), new Task("Task2", user, team));
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAll();

        assertEquals(tasks, result);
    }

    @Test
    public void testCreate() {
        String taskName = "New Task";
        when(teamRepository.findByTeamName("Team1")).thenReturn(Optional.of(team));

        Task newTask = new Task(taskName, user, team);
        when(taskRepository.save(any(Task.class))).thenReturn(newTask);

        Task result = taskService.create("Team1", taskName, user);

        assertEquals(newTask, result);
    }

    @Test
    public void testGetTask_TaskFound() {
        Task task = new Task("Task1", user, team);
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTask(1L);

        assertThat(result).isEqualTo(task);
    }

    @Test
    public void testGetTask_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.getTask(1L));
    }

    @Test
    public void testUpdateName_TaskFound() {
        Task task = new Task("Old Task", user, team);
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task updatedTask = new Task("Updated Task", user, team);
        updatedTask.setId(1L);
        when(taskRepository.save(task)).thenReturn(updatedTask);

        Task result = taskService.updateName(1L, "Updated Task");

        assertThat(result.getName()).isEqualTo("Updated Task");
        verify(taskRepository).save(task);
    }

    @Test
    public void testUpdateName_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.updateName(1L, "New Name"));
        verify(taskRepository, never()).save(any());
    }

    @Test
    public void testToggleFinished_TaskFound() {
        Task task = new Task("Task", user, team);
        task.setId(1L);
        task.setFinished(false);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task toggledTask = new Task("Task", user, team);
        toggledTask.setId(1L);
        toggledTask.setFinished(true);
        when(taskRepository.save(task)).thenReturn(toggledTask);

        Task result = taskService.toggleFinished(1L);

        assertThat(result.isFinished()).isTrue();
        verify(taskRepository).save(task);
    }

    @Test
    public void testToggleFinished_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.toggleFinished(1L));
    }

    @Test
    public void testDelete_TaskFound() {
        Task task = new Task("Task", user, team);
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        boolean result = taskService.delete(1L);

        assertThat(result).isTrue();
        verify(taskRepository).delete(task);
    }

    @Test
    public void testDelete_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = taskService.delete(1L);

        assertThat(result).isFalse();
        verify(taskRepository, never()).delete(any());
    }
}
