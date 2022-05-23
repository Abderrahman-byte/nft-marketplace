package org.stibits.rnft.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
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

import lombok.Getter;
import lombok.Setter;

// ! Token id is a random string for now but it can be replaced by rvn asset name
// Or adding a another field for rvn asset name may help

@Entity
@Table(name = "token")
@Getter
@Setter
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
	
	@OneToMany(targetEntity = Transaction.class, cascade = CascadeType.ALL, mappedBy = "token")
	private List<Transaction> transactions = new ArrayList<>();

	public Token () {
		this.settings.setTokenId(this.id);
		this.settings.setToken(this);
	}
}
