package com.stibits.rnft.common.api;

import java.util.Calendar;

import lombok.Data;

@Data
public class ProfileDetails {
    private String id;
    private String displayName;
    private String username;
    private String bio;
    private String customUrl;
    private String avatarUrl;
    private String cover;
    private Calendar createdDate;
    private String website;
}
