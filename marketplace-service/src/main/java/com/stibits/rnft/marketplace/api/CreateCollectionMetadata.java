package com.stibits.rnft.marketplace.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CreateCollectionMetadata {
    @NotNull
    @NotBlank
    private String name;
    private String description;    
}
