package com.stibits.rnft.marketplace.api;

import java.util.Calendar;

import lombok.Data;

@Data
public class TransactionDetails {
    String id;
    String toId;
    String fromId;
    String tokenId;
    double price;
    Calendar createdDate;
}
