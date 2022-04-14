package org.merchantech.nftproject.model.bo;

import java.util.Collection;

public class NftCollection {

	String id;
	String name;
	String description;
	String  created_date;
	Collection<NFT_Token> NFTS;
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
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public Collection<NFT_Token> getNFTS() {
		return NFTS;
	}
	public void setNFTS(Collection<NFT_Token> nFTS) {
		NFTS = nFTS;
	}
	public NftCollection(String id, String name, String description, String created_date, Collection<NFT_Token> nFTS) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.created_date = created_date;
		NFTS = nFTS;
	}
	
}
