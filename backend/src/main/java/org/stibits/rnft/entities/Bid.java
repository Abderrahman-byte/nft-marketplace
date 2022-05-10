package org.stibits.rnft.entities;

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

@Entity
@Table(name = "bids")
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
    private Calendar createDate = Calendar.getInstance();

    public Bid () {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Calendar getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Calendar createDate) {
        this.createDate = createDate;
    }

    public Account getFrom() {
        return from;
    }

    public void setFrom(Account from) {
        this.from = from;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Account getTo() {
        return to;
    }

    public void setTo(Account to) {
        this.to = to;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public OfferResponse getResponse() {
        return response;
    }

    public void setResponse(OfferResponse response) {
        this.response = response;
    }
}
