package com.stibits.rnft.gateway.domain;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.stibits.rnft.common.utils.RandomStringGenerator;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "username", name = "unique_username"),
        @UniqueConstraint(columnNames = "email", name = "unique_email"),
})
public class Account {
    @Id
    @Column(length = 25)
    private String id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false;

    @Column(name = "is_active", nullable = false)
    private boolean active = false;

    @Column(name = "is_verified", nullable = false)
    private boolean verified = false;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Calendar createdDate = Calendar.getInstance();

    @OneToOne(targetEntity = Profile.class, mappedBy = "account", optional = true, cascade = CascadeType.ALL)
    private Profile profile = new Profile();

    public Account () {
        this.id = RandomStringGenerator.generateStr(25);
        this.profile.setAccount(this);
        this.profile.setId(this.id);
    }
}