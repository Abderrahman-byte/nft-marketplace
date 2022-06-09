package com.stibits.rnft.marketplace.api;

import java.util.Calendar;

import lombok.Data;

@Data
public class AssetContent {
    private String title;
    private String artist;
    private String fileIpfs;
    private boolean hasSource = false;
    private Calendar createdDate = Calendar.getInstance();
}
