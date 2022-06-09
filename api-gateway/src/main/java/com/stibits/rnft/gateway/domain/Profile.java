package com.stibits.rnft.gateway.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

	@Column
	private String website;
}
