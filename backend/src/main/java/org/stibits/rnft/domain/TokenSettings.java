package org.stibits.rnft.domain;

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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "token_settings")
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
}
