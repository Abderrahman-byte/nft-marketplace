package org.stibits.rnft.entities;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "token_settings")
public class TokenSettings {
    @Id
    @Column(name = "token_id")
    private String tokenId;

    @MapsId
    @JoinColumn(name = "token_id")
    @OneToOne(targetEntity = Token.class)
    private Token token;

    @Column(name = "is_for_sale", nullable = false)
    private boolean isForSale = false;

    @Column(name = "instant_sale", nullable = false)
    private boolean instantSale = false;

    @Column(name = "price")
    private double price;

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Calendar updatedDate = Calendar.getInstance();

    public TokenSettings () {}

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setForSale(boolean isForSale) {
        this.isForSale = isForSale;
    }

    public boolean isForSale() {
        return isForSale;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public boolean isInstantSale() {
        return instantSale;
    }

    public void setInstantSale(boolean instantSale) {
        this.instantSale = instantSale;
    }

    public Calendar getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Calendar updatedDate) {
        this.updatedDate = updatedDate;
    }
}
