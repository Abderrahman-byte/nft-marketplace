package org.stibits.rnft.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.CascadeType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="nft_collection")
@Getter
@Setter
@NoArgsConstructor
public class NftCollection {
	@Id
	private String id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "image_url")
	private String imageUrl;

	@ManyToOne(targetEntity = Account.class, optional = false)
	@JoinColumn(name = "created_by", nullable = false)
	private Account createdBy;

	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Calendar createdDate = Calendar.getInstance();

	@OneToMany(targetEntity = Token.class, mappedBy = "collection", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Token> nfts = new ArrayList<>();
}
