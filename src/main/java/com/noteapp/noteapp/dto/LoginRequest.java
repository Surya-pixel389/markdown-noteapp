package com.noteapp.noteapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Email is required")   //message to not leave it blank, fucker
    @Email(message = "Email must be valid")    //another annotation with same shit
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
