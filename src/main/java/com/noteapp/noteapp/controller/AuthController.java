package com.noteapp.noteapp.controller;

import com.noteapp.noteapp.dto.AuthResponse;
import com.noteapp.noteapp.dto.LoginRequest;
import com.noteapp.noteapp.dto.SignupRequest;
import com.noteapp.noteapp.entity.User;
import com.noteapp.noteapp.exception.UnauthorizedException;
import com.noteapp.noteapp.service.JwtService;
import com.noteapp.noteapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:3000")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        User user = userService.registerUser(signupRequest);
        String token = jwtService.generateToken(user.getEmail(), user.getId());

        AuthResponse authResponse = new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getFullName()
        );
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws UnauthorizedException {
        try{
            Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            User user = userService.findByEmail(loginRequest.getEmail());
            String token = jwtService.generateToken(user.getEmail(), user.getId());

            AuthResponse authResponse = new AuthResponse(
                    token,
                    user.getId(),
                    user.getEmail(),
                    user.getFullName()
            );
            return ResponseEntity.ok(authResponse);
        }catch (AuthenticationException e){
            throw new UnauthorizedException("Invalid email or password");
        }
    }
}
