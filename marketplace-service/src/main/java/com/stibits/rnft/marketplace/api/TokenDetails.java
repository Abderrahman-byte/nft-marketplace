package com.stibits.rnft.marketplace.api;

import java.util.Calendar;

import com.stibits.rnft.common.api.ProfileDetails;

import lombok.Data;

@Data
public class TokenDetails {
    private String id;
    private String title;
    private String description;
    private String previewUrl;
    private Calendar createdDate;
    private boolean isForSale;
    private boolean instantSale;
    private double price;
    private int quantity;
    
    private int likesCount;
    private boolean liked = false;

    private ProfileDetails creator;
    private CollectionDetails collection;
    private ProfileDetails owner;
}
