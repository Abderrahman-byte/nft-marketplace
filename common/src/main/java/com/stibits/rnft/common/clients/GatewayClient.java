package com.stibits.rnft.common.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.stibits.rnft.common.api.ApiSuccessResponse;
import com.stibits.rnft.common.api.ProfileDetails;

@FeignClient(name = "api-gateway", fallback = ClientsFallback.class)
public interface GatewayClient {
    @GetMapping(value = "/api/v1/users/{id}")
    ApiSuccessResponse<ProfileDetails> getUserDetails (@PathVariable("id") String id );
}
