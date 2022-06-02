package com.stibits.rnft.gateway.api;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "The username field is required.")
    private String username;

    @NotBlank(message = "The password field is required.")
    private String password;
}
