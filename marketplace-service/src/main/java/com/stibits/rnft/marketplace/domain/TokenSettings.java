package com.stibits.rnft.marketplace.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TokenSettings {
    @Id
    @Column(name = "token_id")
    private String tokenId;

    @MapsId
    @JoinColumn(name = "token_id")
    @OneToOne(targetEntity = Token.class)
    private Token token;

    @Column(nullable = false)
    private boolean isForSale = false;

    @Column(nullable = false)
    private boolean instantSale = false;

    @Column(nullable = false)
    private boolean unlockable = false;

    @Column
    private double price;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Calendar updatedDate = Calendar.getInstance();
}
