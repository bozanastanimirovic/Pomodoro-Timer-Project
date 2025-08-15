package com.example.backend.services;

import com.example.backend.models.Task;
import com.example.backend.models.Team;
import com.example.backend.models.User;
import com.example.backend.repositories.TaskRepository;
import com.example.backend.repositories.TeamRepository;
import com.example.backend.services.impl.TaskService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TeamRepository teamRepository;

    @Override
    public List<Task> getAll() { return taskRepository.findAll(); }

    @Override
    public Task create(String taskTeamName, String taskName, User user) {
        Team team = null;
        if (taskTeamName != null) {
            team = teamRepository.findByTeamName(taskTeamName).orElse(null);
        }
        return taskRepository.save(new Task(taskName, user, team));
    }

    @Override
    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task not found"));
    }

    @Override
    public Task updateName(Long taskId, String taskName) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        task.setName(taskName);
        return taskRepository.save(task);
    }

    @Override
    public Task toggleFinished(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        task.setFinished(!task.isFinished());
        return taskRepository.save(task);
    }

    @Override
    public boolean delete(Long id) {
        taskRepository.findById(id)
                .map(task -> {
                    taskRepository.delete(task);
                    return true;
                })
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        return false;
    }

    @Override
    public List<Task> getByUser(User user) {
        return taskRepository.findByUser(user);
    }
}
