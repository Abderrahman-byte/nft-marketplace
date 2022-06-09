package com.stibits.rnft.gateway.api;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.stibits.rnft.common.anotations.StrongPassword;

import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "The username field is required.")
    @Pattern(message = "The username must start only with letters and must contain at least 5 characters.", regexp = "^[A-Za-z].{4,}$")
    private String username;

    @NotBlank(message = "The username field is required.")
    @Email(message = "Invalid email address.")
    private String email;

    @NotBlank(message = "The username field is required.")
    @StrongPassword
    private String password;

    @NotBlank(message = "The username field is required.")
    private String password2;
}
