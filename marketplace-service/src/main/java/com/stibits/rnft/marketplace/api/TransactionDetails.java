package com.stibits.rnft.marketplace.api;

import java.util.Calendar;

import com.stibits.rnft.common.api.ProfileDetails;

import lombok.Data;

@Data
public class TransactionDetails {
    String id;
    String tokenId;
    double price;
    ProfileDetails to;
    ProfileDetails from;
    Calendar createdDate;
}
