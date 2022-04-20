package org.merchantech.nftproject.model.bo;

public class Profile {

	private String id;
	private Account account;
	private String fullName;
	private String photo;
	private String customUrl;
	private String bio;
	private String socialUrl;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getCustomUrl() {
		return customUrl;
	}
	public void setCustomUrl(String customUrl) {
		this.customUrl = customUrl;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public String getSocialUrl() {
		return socialUrl;
	}
	public void setSocialUrl(String socialUrl) {
		this.socialUrl = socialUrl;
	}
	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	
	
	

}
