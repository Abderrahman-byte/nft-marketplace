package com.stibits.rnft.gateway.web;

import com.stibits.rnft.common.api.ApiResponse;
import com.stibits.rnft.common.api.ApiSuccessResponse;
import com.stibits.rnft.gateway.api.UpdateProfileRequest;
import com.stibits.rnft.gateway.domain.Account;
import com.stibits.rnft.gateway.domain.AccountDetails;
import com.stibits.rnft.gateway.domain.Profile;
import com.stibits.rnft.gateway.services.ProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/${app.version}/auth/profile")
public class CurrentUserController {
    @Autowired
    private ProfileService profileService;

    @GetMapping
    public Mono<ApiResponse> getCurrentUser (@AuthenticationPrincipal Mono<AccountDetails> accountDetails ) {
        if (accountDetails == null) return Mono.just(ApiResponse.getFailureResponse());

        return accountDetails
            .flatMap(details -> {
                Account account = details.getAccount();

                if (account == null) return Mono.empty();
                
                return Mono.just(profileService.getProfileDetails(account));
            }).map(data -> {
                ApiResponse body = new ApiSuccessResponse<>(data);
                return body;
            }).defaultIfEmpty(ApiResponse.getFailureResponse());
    }

    @PutMapping
    public Mono<ApiResponse> updateProfile (@AuthenticationPrincipal Mono<AccountDetails> accountDetails, @RequestBody UpdateProfileRequest body) {
        if (accountDetails == null) return Mono.just(ApiResponse.getFailureResponse());

        return accountDetails.flatMap(details -> {
            if (details == null || details.getAccount() == null) return Mono.empty();

            Profile profile = profileService.updateProfile(details.getAccount(), body);

            if (profile == null) return Mono.empty();

            return Mono.just(ApiResponse.getSuccessResponse());
        }).defaultIfEmpty(ApiResponse.getFailureResponse());
    }
}
