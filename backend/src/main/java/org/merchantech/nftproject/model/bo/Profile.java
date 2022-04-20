package org.merchantech.nftproject.model.bo;

import javax.persistence.*;

@Entity
@Table(name="account_profile")
public class Profile {

	
	@Id
    @Column(name = "account_id")
    private String id;

    @MapsId
    @OneToOne(targetEntity = Account.class)
    @JoinColumn(name = "account_id")
    private Account account;
	
	private String display_name;
	private String bio;
	private String custom_url;
	private String avatar_url;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public String getDisplay_name() {
		return display_name;
	}
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}
	public String getAvatar_url() {
		return avatar_url;
	}
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	public String getCustom_url() {
		return custom_url;
	}
	public void setCustom_url(String custom_url) {
		this.custom_url = custom_url;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	
	
	
	
	
	

}
