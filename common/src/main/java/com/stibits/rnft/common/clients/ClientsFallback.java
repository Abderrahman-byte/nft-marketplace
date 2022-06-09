package com.stibits.rnft.common.clients;

import com.stibits.rnft.common.api.ApiSuccessResponse;
import com.stibits.rnft.common.api.ProfileDetails;

public class ClientsFallback implements GatewayClient {
    @Override
    public ApiSuccessResponse<ProfileDetails> getUserDetails(String id) {
        return null;
    }
}
