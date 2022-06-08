package com.stibits.rnft.marketplace.api;

import java.util.Calendar;
import java.util.List;

import com.stibits.rnft.common.api.ProfileDetails;

import lombok.Data;

@Data
public class CollectionDetails {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private Calendar createdDate;
    private ProfileDetails creator;
    private List<TokenDetails> Tokens;
}
