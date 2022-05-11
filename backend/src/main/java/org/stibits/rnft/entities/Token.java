package org.stibits.rnft.entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import javax.persistence.Column;

import org.hibernate.annotations.CreationTimestamp;

// ! Token id is a random string for now but it can be replaced by rvn asset name
// Or adding a another field for rvn asset name may help

@Entity
@Table(name = "token")
public class Token {
	@Id
	private String id;
	
	@Column(name = "title", nullable = false, unique = true)
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "preview_url", nullable = false)
	private String previewUrl;

	@Column(name = "created_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Calendar createdDate = Calendar.getInstance();

	@ManyToOne(targetEntity = Account.class, optional = false)
	@JoinColumn(name = "artist_id", nullable = false)
	private Account creator;

	@OneToMany(targetEntity = Bid.class, mappedBy = "token")
	private List<Bid> bids = new ArrayList<>();

	@OneToOne(targetEntity = TokenSettings.class, optional = false, orphanRemoval = true, mappedBy = "token", cascade = CascadeType.ALL)
	private TokenSettings settings = new TokenSettings();

	// This field does not have any relation with token 
	// but it can be deducted from transactions in case of single token,
	// after implementing multiple tokens it must be removed or replaced by list of owners
	@Transient
	private Account owner;

	@ManyToOne(targetEntity = NftCollection.class, optional = true)
	@JoinColumn(name = "collection_id", nullable = true)
	private NftCollection collection;

	@OneToMany(targetEntity = TokenLike.class, cascade = CascadeType.ALL, mappedBy = "token")
	private List<TokenLike> likes = new ArrayList<>();
	
	@OneToMany(targetEntity = Transaction.class, cascade = CascadeType.ALL, mappedBy = "token", fetch = FetchType.EAGER)
	private List<Transaction> transactions = new ArrayList<>();

	public Token() {
		this.settings.setToken(this);
	}

	public Calendar getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		this.settings.setTokenId(id);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public NftCollection getCollection() {
		return collection;
	}

	public void setCollection(NftCollection collection) {
		this.collection = collection;
	}

	public Account getCreator() {
		return creator;
	}

	public void setCreator(Account creator) {
		this.creator = creator;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	public List<TokenLike> getLikes() {
		return likes;
	}

	public void setLikes(List<TokenLike> likes) {
		this.likes = likes;
	}

	public TokenSettings getSettings() {
		return settings;
	}

	public void setSettings(TokenSettings settings) {
		this.settings = settings;
	}

	public List<Bid> getBids() {
		return bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

	public List<Transaction> getTransaction() {
		return transactions;
	}

	public void setTransaction(List<Transaction> transaction) {
		this.transactions = transaction;
	}
}
