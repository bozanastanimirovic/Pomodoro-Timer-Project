package com.example.backend.unitTests.controllers;

import com.example.backend.controllers.TeamController;
import com.example.backend.models.Team;
import com.example.backend.services.TeamServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TeamControllerTest {

    @Mock
    private TeamServiceImpl teamService;

    @InjectMocks
    private TeamController teamController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(teamController).build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ROLE_ADMIN"})
    public void testGetAllTeamsAsAdmin() throws Exception {
        List<Team> teams = Arrays.asList(new Team("Test1"), new Team("Test2"));
        when(teamService.getAll()).thenReturn(teams);

        mockMvc.perform(get("/api/team")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teamName").value("Test1"))
                .andExpect(jsonPath("$[1].teamName").value("Test2"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ROLE_ADMIN"})
    public void testCreateTeam() throws Exception {
        String teamName = "New Team";
        Team team = new Team(teamName);
        when(teamService.create(any(Team.class))).thenReturn(team);

        mockMvc.perform(post("/api/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"teamName\":\"" + teamName + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.teamName").value(teamName));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUpdateTeamName() throws Exception {
        Long teamId = 1L;
        String newTeamName = "Updated Team";
        Team updatedTeam = new Team(newTeamName);

        when(teamService.updateName(newTeamName, teamId)).thenReturn(updatedTeam);

        mockMvc.perform(put("/api/team/{id}", teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"teamName\":\"" + newTeamName + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamName").value(newTeamName));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeleteTeam() throws Exception {
        Team team = new Team("Team");
        Long teamId = 1L;
        team.setId(teamId);
        when(teamService.existsById(teamId)).thenReturn(true);

        mockMvc.perform(delete("/api/team/{id}", teamId))
                .andExpect(status().isOk())
                .andExpect(content().string("Resource with ID: " + teamId + " has been successfully deleted"));
    }

}
