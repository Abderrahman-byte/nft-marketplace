package com.stibits.rnft.common.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ApiFailureResponse<T> implements ApiResponse {
    @Getter @Setter private T error;

    public ApiFailureResponse (T error) {
        this.error = error;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }
}
