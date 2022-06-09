package com.stibits.rnft.gateway.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stibits.rnft.common.api.ApiSuccessResponse;
import com.stibits.rnft.common.api.ProfileDetails;
import com.stibits.rnft.common.errors.ApiError;
import com.stibits.rnft.common.errors.NotFoundError;
import com.stibits.rnft.gateway.services.ProfileService;

@RestController
@RequestMapping("/api/${app.version}/users")
public class UsersController {
    @Autowired
    private ProfileService profileService;

    @GetMapping("/{id}")
    public ApiSuccessResponse<ProfileDetails> getUserInfo (@PathVariable String id) throws ApiError {
        ProfileDetails details = profileService.getProfileDetails(id);

        if (details == null) throw new NotFoundError();

        return new ApiSuccessResponse<>(details);
    }
}
