package org.stibits.rnft.entities;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "transactions")
@IdClass(TransactionId.class)
public class Transaction implements Serializable {
    
	@Id
	@Column(name="from_account",nullable = false)
	private String accountFrom;
	
	@Id
	@Column(name="to_account",nullable = false)
	private String accountTo;
	
	@Id
	@Column(name="token_id", nullable = false)
	private String tokenId;
	
	@ManyToOne(targetEntity = Account.class, optional = false)
	@JoinColumn(name = "to_account")
	@MapsId("accountTo")
	private Account account;
	
	@ManyToOne(targetEntity = Token.class, optional = false)
	@JoinColumn(name="token_id")
	@MapsId("tokenId")
	private Token token;
	
	@Column(name="price")
	private double price;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "created_date")
    @CreationTimestamp
	private Calendar createdDate = Calendar.getInstance();
	
	public Transaction() {
		
	}
	public String getAccountFrom() {
		return accountFrom;
	}
	public void setAccountFrom(String accountFrom) {
		this.accountFrom = accountFrom;
	}
	public String getAccountTo() {
		return accountTo;
	}
	public void setAccountTo(String accountTo) {
		this.accountTo = accountTo;
	}
	public String getTokenid() {
		return tokenId;
	}

	public void setTokenid(String tokenid) {
		this.tokenId = tokenid;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Token getToken() {
		return token;
	}
	public void setToken(Token token) {
		this.token = token;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Calendar getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}
	
}
