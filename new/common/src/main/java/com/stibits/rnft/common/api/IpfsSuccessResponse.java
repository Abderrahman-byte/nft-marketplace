package com.stibits.rnft.common.api;

import java.util.Map;

import lombok.Data;

@Data
public class IpfsSuccessResponse {
    private String statusCode;
    private int statusCodeValue;
    private Map<String, String> headers;
    private IpfsSuccessResponseBody body;

    @Data
    public static class IpfsSuccessResponseBody {
        private String stibitsipfs;
    }
}
