package com.noteapp.noteapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {   //what i ask from user is sign up

    @NotBlank(message ="Name is required")
    @Size(min = 2, max = 50, message = "Name is required ")
    @Pattern(regexp = "^[a-z,A-Z]+$", message = "Name should be Vaild")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6,max = 20, message = "Password must contain atleast 6 character")//this annotation is to specify the size for password
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
            message = "Password must contain at least one uppercase letter,one number and one special letter"
    )
    private String password;

}
