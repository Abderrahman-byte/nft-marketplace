package org.stibits.rnft.model.bo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.Column;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "nft_token")
public class NFToken {
	@Id
	private String id;
	
	@Column(name = "title", nullable = false, unique = true)
	private String title;

	@Column(name = "price", nullable = false)
	private double price = 0;

	@Column(name = "description")
	private String description;

	@Column(name = "preview_url", nullable = false)
	private String previewUrl;

	@Column(name = "is_for_sell", nullable = false)
	private boolean forSell;

	@Column(name = "created_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Calendar createdDate = Calendar.getInstance();

	@ManyToOne(targetEntity = Account.class, optional = false)
	@JoinColumn(name = "artist_id", nullable = false)
	private Account creator;

	@ManyToOne(targetEntity = Account.class, optional = false)
	@JoinColumn(name = "owner_id", nullable = false)
	private Account owner;

	@ManyToOne(targetEntity = NftCollection.class, optional = true)
	@JoinColumn(name = "collection_id", nullable = true)
	private NftCollection collection;

	@OneToMany(targetEntity = NftLike.class, cascade = CascadeType.ALL, mappedBy = "token")
	private List<NftLike> likes = new ArrayList<>();

	public NFToken() {
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
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public NftCollection getCollection() {
		return collection;
	}

	public void setCollection(NftCollection collection) {
		this.collection = collection;
	}

	public boolean isForSell() {
		return forSell;
	}

	public void setForSell(boolean forSell) {
		this.forSell = forSell;
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

	public List<NftLike> getLikes() {
		return likes;
	}

	public void setLikes(List<NftLike> likes) {
		this.likes = likes;
	}
}
