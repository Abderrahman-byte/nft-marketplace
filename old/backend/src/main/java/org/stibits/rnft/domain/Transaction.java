package org.stibits.rnft.domain;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction implements Serializable {
	@Id
	private String id;
	
	@ManyToOne(targetEntity = Account.class, optional = false)
	@JoinColumn(name = "from_account")
	private Account from;
	
	@ManyToOne(targetEntity = Account.class, optional = false)
	@JoinColumn(name = "to_account")
	private Account to;
	
	@ManyToOne(targetEntity = Token.class, optional = false)
	@JoinColumn(name="token_id")
	private Token token;
	
	@Column(name="price", nullable = false)
	private double price;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "created_date")
    @CreationTimestamp
	private Calendar createdDate = Calendar.getInstance();
}
