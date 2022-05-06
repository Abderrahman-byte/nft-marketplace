package org.stibits.rnft.entities;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "token_likes")
@IdClass(LikeId.class)
public class TokenLike {
    @Id
    @Column(name = "account_id")
    private String accountId;

    @Id
    @Column(name = "token_id")
    private String tokenId;

    @ManyToOne(targetEntity = Account.class, optional = false)
    @JoinColumn(name = "account_id")
    @MapsId("accountId")
    private Account account;
    
    @ManyToOne(targetEntity = Token.class, optional = false)
    @JoinColumn(name = "token_id")
    @MapsId("tokenId")
    private Token token;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "created_date")
    @CreationTimestamp
    private Calendar createdDate = Calendar.getInstance();

    public TokenLike () {}

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Calendar createdDate) {
        this.createdDate = createdDate;
    }
}
