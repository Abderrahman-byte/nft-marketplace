package com.stibits.rnft.marketplace.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@IdClass(LikeId.class)
@Getter
@Setter
@NoArgsConstructor
public class TokenLike {
    @Id
    @Column(name = "account_id")
    private String accountId;

    @Id
    @Column(name = "token_id")
    private String tokenId;
    
    @ManyToOne(targetEntity = Token.class, optional = false)
    @JoinColumn(name = "token_id")
    @MapsId("tokenId")
    private Token token;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @CreationTimestamp
    private Calendar createdDate = Calendar.getInstance();
}
