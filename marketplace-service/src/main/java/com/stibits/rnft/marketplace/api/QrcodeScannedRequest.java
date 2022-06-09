package com.stibits.rnft.marketplace.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class QrcodeScannedRequest {
    @NotNull
    @NotBlank
    private String code;
    private boolean accepted = false;
}
