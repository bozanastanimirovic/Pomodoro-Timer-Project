package com.example.backend.unitTests.controllers;

import com.example.backend.controllers.UserController;
import com.example.backend.models.Role;
import com.example.backend.models.User;
import com.example.backend.models.enums.UserRole;
import com.example.backend.services.TeamServiceImpl;
import com.example.backend.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private TeamServiceImpl teamService;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(
                new User("User1", "user@example.com", "Name", "Surname", "password"),
                new User("User2", "user2@example.com", "Name2", "Surname2", "password2"));
        when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/api/user/manager")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("User1"))
                .andExpect(jsonPath("$[1].username").value("User2"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetUserById() throws Exception {
        Long userId = 1L;
        User user = new User("User1", "user@example.com", "Name", "Surname", "password");
        when(userService.getById(userId)).thenReturn(user);

        mockMvc.perform(get("/api/user/manager/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("User1"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetUserByIdNotFound() throws Exception {
        Long userId = 1L;
        when(userService.getById(userId)).thenReturn(null);

        mockMvc.perform(get("/api/user/manager/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Resource with requested ID: " + userId + " doesn't exist"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUpdateUserManager() throws Exception {
        Long userId = 1L;
        Role newRole = new Role();
        newRole.setRoleName(UserRole.ROLE_ADMIN);
        User updatedUser = new User("User1", "user@example.com", "Name", "Surname", "password");
        updatedUser.setRole(newRole);


        when(userService.updateRole(newRole.getRoleName(), userId)).thenReturn(updatedUser);

        mockMvc.perform(put("/api/user/manager/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"" + newRole.getRoleName() + "\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role.roleName").value(newRole.getRoleName().toString()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeleteUserManager() throws Exception {
        Long userId = 1L;
        when(userService.existsById(userId)).thenReturn(true);
        doNothing().when(userService).delete(userId);

        mockMvc.perform(delete("/api/user/manager/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Resource with ID: " + userId + " has been successfully deleted"));
    }
}
