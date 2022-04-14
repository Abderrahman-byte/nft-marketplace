package org.merchantech.nftproject.model.bo;

public class NFT_Token {
	private String id;
	private String title;
	private Double price;
	private String preview_url;
	private String files_zip_url;
	private boolean is_for_sell;
	private NftCollection collection;
	private String date;
	
     public NFT_Token(String id, String title, Double price, String preview_url, String files_zip_url,
			boolean is_for_sell, NftCollection collection, String date) {
		super();
		this.id = id;
		this.title = title;
		this.price = price;
		this.preview_url = preview_url;
		this.files_zip_url = files_zip_url;
		this.is_for_sell = is_for_sell;
		this.collection = collection;
		this.date = date;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
