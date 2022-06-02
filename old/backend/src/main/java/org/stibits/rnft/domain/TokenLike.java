package org.stibits.rnft.domain;

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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "token_likes")
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
}
