package com.stibits.rnft.marketplace.api;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CreateTokenMetadata {
    @NotNull
    @NotBlank
    private String title;
    private String description;
    private boolean isForSale = false;
    private boolean instantSale = false;

    @DecimalMin("0")
    private BigDecimal price = new BigDecimal(0);

    private String collectionId;
}
