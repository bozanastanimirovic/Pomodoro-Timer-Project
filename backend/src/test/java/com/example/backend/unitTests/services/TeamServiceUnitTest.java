package com.example.backend.unitTests.services;

import com.example.backend.models.Team;
import com.example.backend.models.User;
import com.example.backend.repositories.TeamRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.TeamServiceImpl;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceUnitTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TeamServiceImpl teamService;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testGetAll() {
        List<Team> teams = Arrays.asList(new Team("Test1"), new Team("Test2"));
        when(teamRepository.findAll()).thenReturn(teams);

        List<Team> result = teamService.getAll();

        assertThat(result).containsExactlyInAnyOrderElementsOf(teams);

        verify(teamRepository).findAll();
    }

    @Test
    public void testGetById_TeamFound() {
        Team team = new Team();
        team.setId(1L);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        Team result = teamService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    public void testCreateTeam() {
        Team team = new Team("Test");
        when(teamRepository.save(team)).thenReturn(team);

        Team result = teamService.create(team);

        assertThat(result).isEqualTo(team);
        verify(teamRepository).save(team);
    }

    @Test
    public void testUpdateTeamName_TeamFound(){
        Team team = new Team("Test");
        team.setId(1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.save(team)).thenReturn(team);

        Team result = teamService.updateName("New Name", 1L);

        assertThat(result.getTeamName()).isEqualTo("New Name");
        verify(teamRepository).save(team);

    }

    @Test
    public void testUpdateTeamName_TeamNotFound(){
        when(teamRepository.findById(2L)).thenReturn(Optional.empty());

        Team result = teamService.updateName("New Name", 2L);

        assertThat(result).isEqualTo(null);
        verify(teamRepository, never()).save(any());
    }

    @Test
    public void testDeleteTeam() {
        Team team = new Team("Test");
        team.setId(1L);

        teamService.delete(1L);

        verify(teamRepository).deleteById(1L);
    }

    @Test
    void testAddUserToTeam_UserAndTeamFound() {
        Team team = new Team("Team1");
        team.setId(1L);
        User user = new User("User1", "user@example.com", "Name", "Surname", "password");
        user.setId(1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(teamRepository.save(team)).thenReturn(team);

        Team result = teamService.addUserToTeam(1L, 1L);

        assertThat(result.getUsers()).contains(user);
        verify(teamRepository).save(team);
    }

    @Test
    void testAddUserToTeam_UserNotFound() {
        Team team = new Team("Team1");
        team.setId(1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> teamService.addUserToTeam(1L, 2L));
        verify(teamRepository, never()).save(any());
    }

    @Test
    void testRemoveUserFromTeam_UserIsMember() {
        Team team = new Team("Team1");
        User user = new User("User1", "user@example.com", "Name", "Surname", "password");
        team.setId(1L);
        user.setId(1L);
        team.getUsers().add(user);
        System.out.println(team.getUsers());

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Team result = teamService.removeUserFromTeam(1L, 1L);

        assertThat(result.getUsers()).doesNotContain(user);
        verify(teamRepository).save(team);
    }



}