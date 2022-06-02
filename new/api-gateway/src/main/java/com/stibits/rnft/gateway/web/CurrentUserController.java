package com.stibits.rnft.gateway.web;

import com.stibits.rnft.common.api.ApiResponse;
import com.stibits.rnft.common.api.ApiSuccessResponse;
import com.stibits.rnft.common.api.ProfileDetails;
import com.stibits.rnft.gateway.domain.Account;
import com.stibits.rnft.gateway.domain.AccountDetails;
import com.stibits.rnft.gateway.domain.Profile;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/${app.version}/auth/user")
public class CurrentUserController {
    @GetMapping
    public Mono<ApiResponse> getCurrentUser (@AuthenticationPrincipal Mono<AccountDetails> accountDetails ) {
        if (accountDetails == null) return Mono.just(ApiResponse.getFailureResponse());

        return accountDetails
            .flatMap(details -> {
                Account account = details.getAccount();

                if (account == null) return Mono.empty();
                
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

                return Mono.just(profileDetails);
            }).map(data -> {
                ApiResponse body = new ApiSuccessResponse<>(data);
                return body;
            }).defaultIfEmpty(ApiResponse.getFailureResponse());
    }
}
