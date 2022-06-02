package com.stibits.rnft.gateway.api;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String displayName;
    private String costumUrl;
    private String bio;
    private String website;
}
