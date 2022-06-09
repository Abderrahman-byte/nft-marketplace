package com.stibits.rnft.marketplace.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.stibits.rnft.common.utils.RandomStringGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Bid {
    @Id
    private String id;

    @Column(nullable = false)
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "offre_response", nullable = false)
    private OfferResponse response = OfferResponse.PENDING;

    @Column(nullable = false)
    private String toId;

    @Column(nullable = false)
    private String fromId;

    @ManyToOne(targetEntity = Token.class, optional = false)
    @JoinColumn(name = "token_id")
    private Token token;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @CreationTimestamp
    private Calendar createdDate = Calendar.getInstance();

    public Bid () {
        this.id = RandomStringGenerator.generateStr();
    }
}
