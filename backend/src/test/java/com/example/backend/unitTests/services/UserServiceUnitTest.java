package com.example.backend.unitTests.services;

import com.example.backend.models.Role;
import com.example.backend.models.User;
import com.example.backend.repositories.RoleRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.UserServiceImpl;
import com.example.backend.models.enums.UserRole;
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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void testGetAllUsers() {
        List<User> users = Arrays.asList(
                new User("User1", "user@example.com", "Name", "Surname", "password"),
                new User("User2", "user2@example.com", "Name2", "Surname2", "password2"));

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();

        assertEquals(users, result);

    }

    @Test
    public void testGetUserById_UserFound(){
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getById(1L);

        assertEquals(user, result);
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    public void testCreateUser() {
        User user = new User("User1", "user@example.com", "Name", "Surname", "password");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.create(user);

        assertThat(result).isEqualTo(user);
        verify(userRepository).save(user);
    }

    @Test
    public void testDeleteTeam() {
        User user = new User("User", "user@example.com", "Name", "Surname", "password");
        user.setId(1L);

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    public void testGetUsersByName() {
        String name = "Test";
        List<User> users = Arrays.asList(
                new User("User1", "user@example.com", name, "Surname", "password"),
                new User("User2", "user2@example.com", name, "Surname2", "password2"));


        when(userRepository.findByNameContainingIgnoreCase(name)).thenReturn(users);

        List<User> result = userService.getUsersByName(name);

        assertEquals(result, users);
    }

    @Test
    void testUpdateRole_UserAndRoleFound() {
        User user = new User("User", "user@example.com", "Name", "Surname", "password");
        user.setId(1L);
        Role role = new Role();
        role.setRoleName(UserRole.ROLE_ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByRoleName(UserRole.ROLE_ADMIN)).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = userService.updateRole(UserRole.ROLE_ADMIN, 1L);

        assertThat(updatedUser.getRole()).isEqualTo(role);
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateRole_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateRole(UserRole.ROLE_ADMIN, 1L));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateRole_RoleNotFound() {
        User user = new User("User", "user@example.com", "Name", "Surname", "password");
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByRoleName(UserRole.ROLE_ADMIN)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateRole(UserRole.ROLE_ADMIN, 1L));
        verify(userRepository, never()).save(any());
    }
}
