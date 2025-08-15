package com.example.backend.services.impl;

import com.example.backend.models.Task;
import com.example.backend.models.User;

import java.util.List;

public interface TaskService {
    List<Task> getAll();

    Task create(String taskTeamName, String taskName, User user);

    Task getTask(Long taskId);

    Task updateName(Long taskId, String taskName);

    Task toggleFinished(Long id);

    boolean delete(Long id);

    List<Task> getByUser(User user);
}
