package org.stibits.rnft.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bids")
@Getter
@Setter
@NoArgsConstructor
public class Bid {
    @Id
    private String id;

    @Column(name = "bid_price", nullable = false)
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "offre_response", nullable = false)
    private OfferResponse response = OfferResponse.PENDING;

    @ManyToOne(targetEntity = Account.class, optional = false)
    @JoinColumn(name = "to_account")
    private Account to;

    @ManyToOne(targetEntity = Account.class, optional = false)
    @JoinColumn(name = "from_account")
    private Account from;

    @ManyToOne(targetEntity = Token.class, optional = false)
    @JoinColumn(name = "token_id")
    private Token token;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    @CreationTimestamp
    private Calendar createdDate = Calendar.getInstance();
}
