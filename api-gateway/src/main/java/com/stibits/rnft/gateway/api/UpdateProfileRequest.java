package com.stibits.rnft.gateway.api;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    @NotBlank(message = "Display name cannot be blank.")
    private String displayName;
    private String costumUrl;
    private String bio;
    private String website;
}
