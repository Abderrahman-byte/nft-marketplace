package com.stibits.rnft.common.api;

public interface ApiResponse {
    public boolean isSuccess ();

    public static ApiResponse getSuccessResponse () {
        return new ApiResponse() {
            @Override
            public boolean isSuccess() {
                return true;
            }
        };
    }

    public static ApiResponse getFailureResponse () {
        return new ApiResponse() {
            @Override
            public boolean isSuccess() {
                return false;
            }
        };
    }
}
