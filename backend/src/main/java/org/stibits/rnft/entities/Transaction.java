package org.stibits.rnft.entities;

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

@Entity
@Table(name = "transactions")
public class Transaction implements Serializable {
	@Id
	private String id;
	
	@ManyToOne(targetEntity = Token.class, optional = false)
	@JoinColumn(name = "from_account")
	private Account from;
	
	@ManyToOne(targetEntity = Token.class, optional = false)
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
	
	public Transaction() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Account getTo() {
		return to;
	}

	public void setTo(Account to) {
		this.to = to;
	}

	public Account getFrom() {
		return from;
	}

	public void setFrom(Account from) {
		this.from = from;
	}

	public Calendar getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}
}
