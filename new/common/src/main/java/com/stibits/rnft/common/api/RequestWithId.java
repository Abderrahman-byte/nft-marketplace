package com.stibits.rnft.common.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RequestWithId {
    @NotNull
    @NotBlank
    private String id;
}
