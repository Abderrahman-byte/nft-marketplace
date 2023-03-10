package com.stibits.rnft.gateway.repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import com.stibits.rnft.gateway.domain.Profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileRepository {
    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Profile save (Profile profile) {
        return this.entityManager.merge(profile);
    }
}
