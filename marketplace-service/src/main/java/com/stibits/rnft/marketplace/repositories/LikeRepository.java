package com.stibits.rnft.marketplace.repositories;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LikeRepository {
    @Autowired
    private EntityManager entityManager;

    @Transactional
    public boolean likeToken (String tokenId, String accountId) {
        String sqlString = "INSERT INTO token_like (token_id, account_id, created_date) VALUES (:id, :accountId, NOW())";

        return this.entityManager
            .createNativeQuery(sqlString)
            .setParameter("id", tokenId)
            .setParameter("accountId", accountId)
            .executeUpdate() > 0;
    }

    @Transactional
    public boolean unlikeToken (String tokenId, String accountId) {
        String sqlString = "DELETE FROM token_like WHERE token_id = :id AND account_id = :accountId ";

        return this.entityManager
            .createNativeQuery(sqlString)
            .setParameter("id", tokenId)
            .setParameter("accountId", accountId)
            .executeUpdate() > 0;
    }
}
