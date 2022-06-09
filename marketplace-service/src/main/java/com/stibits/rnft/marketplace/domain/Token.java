package com.stibits.rnft.marketplace.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import javax.persistence.Column;

import org.hibernate.annotations.CreationTimestamp;

import com.stibits.rnft.common.utils.RandomStringGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = "title", name = "unique_token_title"),
	@UniqueConstraint(columnNames = "assetPath", name = "unique_token_asset_name"),
})
public class Token {
	@Id
	private String id;
	
	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String assetPath; 

	@Column
	private String description;

	@Column(nullable = false)
	private String previewUrl;

	@Column(nullable = false)
	private String ipfs;

	@Column
	private String sourceUrl;

	@Column(nullable = false)
	private String creatorId;

	@Column(nullable = false)
	private int quantity = 1;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Calendar createdDate = Calendar.getInstance();

	@OneToOne(targetEntity = TokenSettings.class, optional = false, orphanRemoval = true, mappedBy = "token", cascade = CascadeType.ALL)
	private TokenSettings settings = new TokenSettings();

	@OneToMany(targetEntity = Bid.class, mappedBy = "token")
	private List<Bid> bids = new ArrayList<>();

	@ManyToOne(targetEntity = Collection.class, optional = true)
	@JoinColumn(name = "collection_id", nullable = true)
	private Collection collection;

	@OneToMany(targetEntity = TokenLike.class, cascade = CascadeType.ALL, mappedBy = "token")
	private List<TokenLike> likes = new ArrayList<>();
	
	@OneToMany(targetEntity = Transaction.class, cascade = CascadeType.ALL, mappedBy = "token")
	private List<Transaction> transactions = new ArrayList<>();

	public Token () {
		this.id = RandomStringGenerator.generateStr();
		this.settings.setTokenId(this.id);
		this.settings.setToken(this);
	}
}
