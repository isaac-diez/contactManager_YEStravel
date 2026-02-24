package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.dto.UserDisplayDTO;
import com.yestravel.contactsmanager.security.CustomUserDetails;
import com.yestravel.contactsmanager.model.Role;
import com.yestravel.contactsmanager.model.User;
import com.yestravel.contactsmanager.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String username, String email, String rawPassword, Role role) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .role(role)
                .build();
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new CustomUserDetails(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<UserDisplayDTO> getAllUsersForDisplay() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserDisplayDTO dto = new UserDisplayDTO();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    return dto;
                })
                .toList();
    }
}

