package com.stibits.rnft.marketplace.api;

import lombok.Data;

@Data
public class UpdateSettingsRequest {
    private Boolean instantSale = null;
    private Boolean isForSale = null;
    private Double price = null;
}
