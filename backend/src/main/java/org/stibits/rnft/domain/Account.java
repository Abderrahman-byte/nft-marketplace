package org.stibits.rnft.domain;

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
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
public class Account {
    @Id
    private String id;
    
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

	@Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false;

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
     
    @Transient
    private List<Token> nfts = new ArrayList<>();

    @OneToMany(targetEntity = Bid.class, mappedBy = "to")
    private List<Bid> receivedBids = new ArrayList<>();

    @OneToMany(targetEntity = Bid.class, mappedBy = "from")
    private List<Bid> sentBids = new ArrayList<>();

    @OneToMany(mappedBy="creator", fetch= FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Token> createdNfts = new ArrayList<>();

    @OneToMany(mappedBy="createdBy", fetch= FetchType.LAZY)
    private List<NftCollection> collections = new ArrayList<>();
    
    @OneToOne(targetEntity = Profile.class, mappedBy = "account", optional = true, cascade = CascadeType.MERGE)
    private Profile profile;
    
    @OneToMany(targetEntity = Transaction.class, mappedBy ="from")
    private List<Transaction> transactionsFrom = new ArrayList<>();

    @OneToMany(targetEntity = Transaction.class, mappedBy ="to")
    private List<Transaction> transactionsTo = new ArrayList<>();
}