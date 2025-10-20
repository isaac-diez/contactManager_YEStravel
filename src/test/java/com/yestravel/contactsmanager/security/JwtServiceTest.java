package com.yestravel.contactsmanager.security;

import com.yestravel.contactsmanager.model.Role;
import com.yestravel.contactsmanager.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    
    @BeforeEach
    void setUp() {
        String testSecret = "testSecretKeyThatIsLongEnoughForHS256Algorithm";
        long testExpirationMs = 86400000;
        jwtService = new JwtService(testSecret, testExpirationMs);
    }

    @AfterEach
    void tearDown() {
        jwtService = null;
    }

    @Test
    void generateUserToken_ShouldReturnToken() {
        // Arrange
        User user = User.builder()
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .role(Role.ROLE_USER)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(user);

        // Act
        String token = jwtService.generateToken(userDetails);

        Logger logger = Logger.getLogger(JwtServiceTest.class.getName());
        logger.info("Generated token: " + token);
        
        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.length() > 30);

    }

    @Test
    void generateAdminToken_ShouldReturnValidToken() {
        // Arrange
        User user = User.builder()
                .username("testadmin")
                .password("password")
                .email("admin@example.com")
                .role(Role.ROLE_ADMIN)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(user);

        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void extractUsername() {
        // Arrange
        User user = User.builder()
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .role(Role.ROLE_USER)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(user);

        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(jwtService.isTokenValid(token, userDetails));
        assertEquals(userDetails.getUsername(), jwtService.extractUsername(token));
    }

    @Test
    void extractClaim_ShouldReturnValidUsernameClaim() {
        // Arrange
        User user = User.builder()
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .role(Role.ROLE_USER)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(user);

        // Act
        String token = jwtService.generateToken(userDetails);

        String usernameFromClaim = jwtService.extractClaim(token, Claims::getSubject);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(jwtService.isTokenValid(token, userDetails));
        assertEquals(userDetails.getUsername(), usernameFromClaim);
    }

    @Test
    void extractClaim_ShouldReturnValidRoleClaim() {
        // Arrange
        User user = User.builder()
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .role(Role.ROLE_USER)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(user);

        // Act
        String token = jwtService.generateToken(userDetails);

        String roleFromClaim = jwtService.extractClaim(token, claims -> claims.get("role", String.class));

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(jwtService.isTokenValid(token, userDetails));
        assertEquals(userDetails.getAuthorities().stream().findFirst().map(Object::toString).orElse(""), roleFromClaim);
    }

    @Test
    void extractClaim_ShouldReturnInvalidTokenException() {

        String invalidToken = "invalidToken";

        assertThrows(JwtException.class, () -> jwtService.extractClaim(invalidToken, Claims::getSubject));

    }

    @Test
    void isTokenValid() {

        User user = User.builder()
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .role(Role.ROLE_USER)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(user);

        String token = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(token, userDetails));

    }
}