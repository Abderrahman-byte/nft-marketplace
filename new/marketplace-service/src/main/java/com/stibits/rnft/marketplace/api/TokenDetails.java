package com.stibits.rnft.marketplace.api;

import java.util.Calendar;

import lombok.Data;

@Data
public class TokenDetails {
    private String id;
    private String title;
    private String description;
    private String previewUrl;
    private String creatorId;
    private Calendar createdDate;
    private boolean isForSale;
    private boolean instantSale;
    private double price;
    private int quantity;
    private int likesCount;
    private boolean liked = false;
    // Object collection;
    // Object creator;
    // Object owner;
}
