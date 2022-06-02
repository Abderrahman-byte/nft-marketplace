package com.stibits.rnft.common.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ApiSuccessResponse<T> implements ApiResponse {
    @Setter @Getter private T data;

    public ApiSuccessResponse (T data) {
        this.data = data;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }
}
