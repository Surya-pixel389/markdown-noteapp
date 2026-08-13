package com.noteapp.noteapp.service;

import com.noteapp.noteapp.dto.SignupRequest;
import com.noteapp.noteapp.entity.User;
import com.noteapp.noteapp.exception.ResourceNotFoundException;
import com.noteapp.noteapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(SignupRequest signupRequest) {
        if(userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = User.builder()
                .email(signupRequest.getEmail())
                .fullName(signupRequest.getFullName())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .build();

        return userRepository.save(user);
    }
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not Found"));
    }

}
