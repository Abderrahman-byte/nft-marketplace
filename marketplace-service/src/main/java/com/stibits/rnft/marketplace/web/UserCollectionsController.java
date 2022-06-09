package com.stibits.rnft.marketplace.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stibits.rnft.common.api.ApiResponse;
import com.stibits.rnft.common.api.ApiSuccessResponse;
import com.stibits.rnft.common.api.ProfileDetails;
import com.stibits.rnft.marketplace.services.CollectionService;

@RestController
@RequestMapping("/user/collections")
public class UserCollectionsController {
    @Autowired
    private CollectionService collectionService;

    @GetMapping
    public ApiResponse getUserCollections (@AuthenticationPrincipal ProfileDetails profile) {
        if (profile == null) return ApiResponse.getFailureResponse();
        
        return new ApiSuccessResponse<>(collectionService.getUserCollections(profile.getId()));
    }
}
