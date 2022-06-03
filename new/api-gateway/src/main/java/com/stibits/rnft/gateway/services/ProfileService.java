package com.stibits.rnft.gateway.services;

import com.stibits.rnft.common.api.ProfileDetails;
import com.stibits.rnft.gateway.api.UpdateProfileRequest;
import com.stibits.rnft.gateway.domain.Account;
import com.stibits.rnft.gateway.domain.Profile;
import com.stibits.rnft.gateway.repository.ProfileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public Profile updateProfile (Account account, UpdateProfileRequest request) {
        return this.updateProfile(account.getProfile(), request);
    }

    public Profile updateProfile (Profile profile, UpdateProfileRequest request) {
        if (StringUtils.hasText(request.getDisplayName())) profile.setDisplayName(request.getDisplayName());
        if (StringUtils.hasText(request.getCostumUrl())) profile.setCustomUrl(request.getCostumUrl());
        if (StringUtils.hasText(request.getBio())) profile.setBio(request.getBio());
        if (StringUtils.hasText(request.getWebsite())) profile.setWebsite(request.getWebsite());

        return this.profileRepository.save(profile);
    } 

    public ProfileDetails getProfileDetails (Account account) {
        Profile profile = account.getProfile();
        ProfileDetails profileDetails = new ProfileDetails();

        profileDetails.setId(account.getId());
        profileDetails.setUsername(account.getUsername());
        profileDetails.setDisplayName(profile.getDisplayName());
        profileDetails.setCustomUrl(profile.getCustomUrl());
        profileDetails.setCreatedDate(account.getCreatedDate());
        profileDetails.setCover(profile.getCoverUrl());
        profileDetails.setBio(profile.getBio());
        profileDetails.setAvatarUrl(profile.getAvatarUrl());
        profileDetails.setWebsite(profile.getWebsite());
        profileDetails.setVerified(account.isVerified());

        return profileDetails;
    }
}
