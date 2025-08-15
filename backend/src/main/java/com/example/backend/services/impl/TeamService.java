package com.example.backend.services.impl;

import com.example.backend.models.Team;
import com.example.backend.models.User;

import java.util.List;

public interface TeamService {
    List<Team> getAll();

    Team getById(Long id);

    boolean existsById(Long id);

    Team create(Team t);

    void delete(Long id);

    java.util.List<User> getTeamMembers(Long teamId);


    void addUsersToTeam(Long teamId, List<Long> userIds);

    Team addUserToTeam(Long teamId, Long userId);

    Team removeUserFromTeam(Long teamId, Long userId);

    boolean existsByTeamName(String teamName);

    Team updateName(String teamName, Long id);
}
