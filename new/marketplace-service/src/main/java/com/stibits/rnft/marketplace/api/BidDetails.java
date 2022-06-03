package com.stibits.rnft.marketplace.api;

import java.util.Calendar;

import lombok.Data;

@Data
public class BidDetails {
    private String id;
    private String response;
    private String toId;
    private String fromId;
    private double price;
    private Calendar createdDate;
}
