package org.stibits.rnft.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;

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
	
	@Column(name = "display_name", nullable = false)
	private String displayName;

	@Column(name = "bio")
	private String bio;

	@Column(name = "custom_url")
	private String customUrl;

	@Column(name = "avatar_url")
	private String avatarUrl;
	
	@Column(name = "cover_url")
	private String coverUrl;

	public Profile () {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}


	public String getCustomUrl() {
		return customUrl;
	}

	public void setCustomUrl(String customUrl) {
		this.customUrl = customUrl;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getCoverUrl() {
		return coverUrl;
	}
	
	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}
}
