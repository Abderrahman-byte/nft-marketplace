package org.stibits.rnft.model.bo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "account")
public class Account {
    @Id
    private String id;
    
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	@Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false;

    @Column(name = "is_artist", nullable = false)
    private boolean isArtist = false;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Calendar createdDate = Calendar.getInstance();

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Calendar updatedDate = Calendar.getInstance();
     
    @OneToMany(mappedBy="owner", fetch= FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<NFToken> nfts = new ArrayList<>();

    @OneToMany(mappedBy="creator", fetch= FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<NFToken> createdNfts = new ArrayList<>();

    @OneToMany(mappedBy="createdBy", fetch= FetchType.LAZY)
    private List<NftCollection> collections = new ArrayList<>();
    
    @JsonManagedReference
    @OneToOne(targetEntity = Profile.class, mappedBy = "account", optional = true, cascade = CascadeType.MERGE)
    private Profile profile;
    
    public Account () {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isArtist() {
        return isArtist;
    }

    public void setArtist(boolean isArtist) {
        this.isArtist = isArtist;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Calendar createdDate) {
        this.createdDate = createdDate;
    }

    public Calendar getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Calendar updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<NFToken> getNfts() {
        return nfts;
    }

    public void setNfts(List<NFToken> nfts) {
        this.nfts = nfts;
    }

    public List<NFToken> getCreatedNfts() {
        return createdNfts;
    }

    public void setCreatedNfts(List<NFToken> createdNfts) {
        this.createdNfts = createdNfts;
    }

    public List<NftCollection> getCollections() {
        return collections;
    }

    public void setCollections(List<NftCollection> collections) {
        this.collections = collections;
    }
}