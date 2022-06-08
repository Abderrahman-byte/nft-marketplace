package com.stibits.rnft.marketplace.api;

import java.util.Calendar;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StreamReferencePayload {
    private String toId;
    private String fromId;
    private String action;    
    private double price;
    private Calendar exp = Calendar.getInstance();

    public StreamReferencePayload (String toId, String fromId, String action, double price) {
        this.toId = toId;
        this.fromId = fromId;
        this.action = action;
        this.price = price;
    }
}
