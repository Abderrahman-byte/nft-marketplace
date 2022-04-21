package org.merchantech.nftproject.model.bo;

import java.util.Calendar;
import java.util.Collection;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

// TODO : Refactor

@Entity
@Table(name="nft_collection")
public class NftCollection {

	@Id
	private String id;
	@Column(name="name", nullable=false, unique=true)
	private String name;
	@Column(name="description")
	private String description;
	
	@Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Calendar createdDate = Calendar.getInstance();
	
	@OneToMany(mappedBy="collection", fetch= FetchType.LAZY)
	private Collection<NftToken> NFTS;
	
	@ManyToOne
	@JoinColumn(name="created_by")
	private Account account;
	
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public NftCollection() {
		
	}
	
	public NftCollection(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}
	
	
	public Calendar getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Collection<NftToken> getNFTS() {
		return NFTS;
	}
	public void setNFTS(Collection<NftToken> nFTS) {
		NFTS = nFTS;
	}
	/*public NftCollection(String id, String name, String description, String created_date, Collection<NFT_Token> nFTS) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		//this.created_date = created_date;
		NFTS = nFTS;
	}*/
	
	
}
