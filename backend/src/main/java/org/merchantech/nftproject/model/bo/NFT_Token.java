package org.merchantech.nftproject.model.bo;
import java.util.Calendar;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;


@Entity
@Table(name="nft_token")
public class NFT_Token {
	
	@Id
	private String id;
	
	@Column (name="title", nullable=false, unique=true)
	private String title;
	@Column(name="price",nullable=false)
	private Double price;
	@Column(name="preview_url", nullable=false, unique=true)
	private String preview_url;
	@Column(name="files_zip_url", nullable=false, unique=true)
	private String files_zip_url;
	@Column(name="is_for_sell", nullable=false)
	private boolean is_for_sell;
	
	@Column(name = "created_date", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Calendar createdDate = Calendar.getInstance();
	
	
	@ManyToOne
	@JoinColumn(name="artist_id")
	private Account account;
	
	@ManyToOne
	@JoinColumn(name="owner_id", nullable = true)
	private Account owner;
	
	@ManyToOne
	@JoinColumn(name="collection_id",nullable=true)
	private NftCollection collection;

	
	
	
	public NFT_Token() {
		
	}
	
	public NFT_Token(String title, Double price, String preview_url, String files_zip_url, boolean is_for_sell,Account account ) {
		super();
		this.title = title;
		this.price = price;
		this.preview_url = preview_url;
		this.files_zip_url = files_zip_url;
		this.is_for_sell = is_for_sell;
		this.account = account;
		
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getPreview_url() {
		return preview_url;
	}
	public void setPreview_url(String preview_url) {
		this.preview_url = preview_url;
	}
	public String getFiles_zip_url() {
		return files_zip_url;
	}
	public void setFiles_zip_url(String files_zip_url) {
		this.files_zip_url = files_zip_url;
	}
	public boolean isIs_for_sell() {
		return is_for_sell;
	}
	public void setIs_for_sell(boolean is_for_sell) {
		this.is_for_sell = is_for_sell;
	}
	public NftCollection getCollection() {
		return collection;
	}
	public void setCollection(NftCollection collection) {
		this.collection = collection;
	}
	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}

}
