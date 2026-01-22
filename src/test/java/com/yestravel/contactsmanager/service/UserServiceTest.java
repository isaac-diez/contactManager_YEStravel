package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.model.User;
import com.yestravel.contactsmanager.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.yestravel.contactsmanager.model.Role.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserService userService;

    @Test
    void registerNewUserRoleAdmin() {

        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testAdmin");
        testUser.setPassword("test");
        testUser.setRole(ROLE_ADMIN);

        when(userRepo.existsByUsername("testAdmin")).thenReturn(false);

        when(passwordEncoder.encode(("test"))).thenReturn("encodedPassTest");

        when(userRepo.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setId(1L);
            return userToSave;
                });

        User result = userService.register("testAdmin","null","test", ROLE_ADMIN);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testAdmin", result.getUsername());
        assertEquals("encodedPassTest", result.getPassword());
        assertEquals(ROLE_ADMIN, result.getRole());

        verify(userRepo,times(1)).save(result);
        verify(passwordEncoder,times(1)).encode("test");
        verify(userRepo, times(1)).existsByUsername("testAdmin");

    }

    @Test
    void registerNewUserRoleUser() {

        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setPassword("test");
        testUser.setRole(ROLE_USER);

        when(userRepo.existsByUsername("testUser")).thenReturn(false);

        when(passwordEncoder.encode(("test"))).thenReturn("encodedPassTest");

        when(userRepo.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setId(1L);
            return userToSave;
        });

        User result = userService.register("testUser","null","test", ROLE_USER);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testUser", result.getUsername());
        assertEquals("encodedPassTest", result.getPassword());
        assertEquals(ROLE_USER, result.getRole());

        verify(userRepo,times(1)).save(result);
        verify(passwordEncoder,times(1)).encode("test");
        verify(userRepo, times(1)).existsByUsername("testUser");


    }

    @Test
    void register_ShouldThrowException_WhenUsernameExists(){

        when(userRepo.existsByUsername("existingUsername")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.register("existingUsername","email@test.com","test",ROLE_USER);
        });

        verify(userRepo,never()).save(any(User.class));
    }

    @Test
    void loadExistingUserByUsername_Success() {

        User mockExistingUser = User.builder()
                .username("existingUser")
                .password("passExistingUser")
                .role(ROLE_ADMIN)
                .build();

        when(userRepo.findByUsername("existingUser")).thenReturn(Optional.of(mockExistingUser));

        UserDetails result = userService.loadUserByUsername("existingUser");

        assertNotNull(result);
        assertEquals(mockExistingUser.getUsername(), result.getUsername());
        assertInstanceOf(UserDetails.class, result);

        verify(userRepo, times(1)).findByUsername("existingUser");

    }

    @Test
    void loadNonExistingUserByUsernameShouldReturnException() {

        when(userRepo.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonExistingUser");
            });

        verify(userRepo, times(1)).findByUsername("nonExistingUser");

    }

    @Test
    void getUserById_Success() {

        User mockExistingUser = User.builder()
                .username("existingUser")
                .password("passExistingUser")
                .role(ROLE_ADMIN)
                .build();

        when(userRepo.findById(1L)).thenReturn(Optional.of(mockExistingUser));

        User found = userService.getUserById(1L);

        assertEquals(mockExistingUser, found);
        verify(userRepo, times(1)).findById(1L);
    }

    @Test
    void getUserById_ShouldReturnException() {

        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userService.getUserById(1L);
        });

        verify(userRepo, times(1)).findById(1L);
    }
}