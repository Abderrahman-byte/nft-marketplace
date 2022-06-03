package com.stibits.rnft.marketplace.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;

import com.stibits.rnft.common.utils.RandomStringGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = "name", name = "unique_collection_name")
})
public class Collection {
	@Id
	private String id;

	@Column(nullable = false)
	private String name;

	@Column
	private String description;

	@Column
	private String imageUrl;

	@Column
	private String createdById;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Calendar createdDate = Calendar.getInstance();

	@OneToMany(targetEntity = Token.class, mappedBy = "collection", fetch = FetchType.LAZY)
	private List<Token> nfts = new ArrayList<>();

	public Collection () {
		this.id = RandomStringGenerator.generateStr();
	}
}
