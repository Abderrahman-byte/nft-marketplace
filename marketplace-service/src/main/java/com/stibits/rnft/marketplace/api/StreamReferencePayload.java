package com.stibits.rnft.marketplace.api;

import java.util.Calendar;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StreamReferencePayload {
    private String tokenId;
    private String toId;
    private String fromId;
    private String action;
    private double price;
    private String tokenTitle;
    private String previewUrl;
    private long iat = Calendar.getInstance().getTimeInMillis() / 1000;
    private long exp;
    
    public void expiresAfterMinutes (int minutes) {
        Calendar exp = Calendar.getInstance();
        exp.add(Calendar.MINUTE, minutes);
        
        this.expiresAt(exp);
    }

    public void expiresAt (Calendar exp) {
        this.exp = exp.getTimeInMillis() / 1000;
    }
}
