package com.stibits.rnft.marketplace.repositories;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.stibits.rnft.marketplace.domain.Token;

@Repository
public class TokenRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaBuilder criteriaBuilder;

    @PostConstruct
    public void getCriteriaBuilder () {
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Token selectById (String id) {
        return this.entityManager.find(Token.class, id);
    }

    public Token selectByTitle (String title) {
        CriteriaQuery<Token> query = this.criteriaBuilder.createQuery(Token.class);
        Root<Token> token = query.from(Token.class);

        query.select(token).where(
            this.criteriaBuilder.equal(token.get("title"), title)
        );

        try {
            return this.entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            return null;
        }
    }

    public List<Token> selectTokensSortedByHighPrice (int limit, int offset, double maxPrice) {
        CriteriaQuery<Token> query = this.criteriaBuilder.createQuery(Token.class);
        Root<Token> token = query.from(Token.class);

        query.select(token).where(
            this.criteriaBuilder.lessThanOrEqualTo(token.get("settings").get("price"), maxPrice)
        ).orderBy(
            this.criteriaBuilder.desc(token.get("settings").get("price"))
        );

        return this.entityManager.createQuery(query).setMaxResults(limit).setFirstResult(offset).getResultList();
    }

    public List<Token> selectTokensSortedByLowPrice (int limit, int offset, double maxPrice) {
        CriteriaQuery<Token> query = this.criteriaBuilder.createQuery(Token.class);
        Root<Token> token = query.from(Token.class);

        query.select(token).where(
            this.criteriaBuilder.lessThanOrEqualTo(token.get("settings").get("price"), maxPrice)
        ).orderBy(
            this.criteriaBuilder.asc(token.get("settings").get("price"))
        );

        return this.entityManager.createQuery(query).setMaxResults(limit).setFirstResult(offset).getResultList();
    }

    public List<Token> selectTokensSortedByPopularity (int limit, int offset, double maxPrice) {
        String sqlString = "SELECT t.id, t.asset_path, t.created_date, t.creator_id, t.description, t.preview_url, t.title, t.collection_id, t.quantity, t.source_url, t.ipfs, " +
        "COUNT(lk.account_id) as likes_count, settings.price as token_price, COUNT(bid.id) as bids_count " +
        "FROM token as t " +
        "LEFT JOIN token_like as lk ON lk.token_id = t.id " +
        "LEFT JOIN bid ON bid.token_id = bid.token_id and bid.created_date <= NOW() - INTERVAL '1 MONTH' " +
        "JOIN token_settings as settings ON settings.token_id = t.id " +
        "WHERE settings.price <= :maxPrice " +
        "GROUP BY t.id, t.asset_path, t.created_date, t.creator_id, t.description, t.preview_url, t.title, t.collection_id, t.quantity, t.source_url, token_price, t.ipfs " +
        "ORDER BY bids_count DESC, likes_count DESC , created_date DESC " +
        "LIMIT :limit OFFSET :offset ";
        
        Map<String, Object> attributes = Map.of("limit", limit, "offset", offset, "maxPrice", maxPrice);
        
        return this.selectTokensWithNativeQuery(sqlString, attributes);
    }
    
    public List<Token> selectTokensSortedByLikes (int limit, int offset, double maxPrice) {
        String sqlString = "SELECT t.id, t.asset_path, t.created_date, t.creator_id, t.description, t.preview_url, t.title, t.collection_id, t.quantity, t.source_url, t.ipfs, " +
        "COUNT(lk.account_id) AS likes_count, settings.price AS price "+
        "FROM token AS t " +
        "LEFT JOIN token_like AS lk ON lk.token_id = t.id " +
        "JOIN token_settings AS settings ON settings.token_id = t.id " +
        "WHERE price <= :maxPrice "+
        "GROUP BY t.id, t.asset_path, t.created_date, t.creator_id, t.description, t.preview_url, t.title, t.collection_id, t.quantity, t.source_url, t.ipfs, price "+
        "ORDER BY likes_count DESC , created_date DESC " +
        "LIMIT :limit OFFSET :offset ";

        Map<String, Object> attributes = Map.of("limit", limit, "offset", offset, "maxPrice", maxPrice);

        return this.selectTokensWithNativeQuery(sqlString, attributes);
    }

    @Transactional
    public Token save (Token token) {
        this.entityManager.persist(token);
        
        return token;
    }

    @SuppressWarnings("unchecked")
    private List<Token> selectTokensWithNativeQuery (String sqlString, Map<String, Object> attributes) {
        Query query = this.entityManager.createNativeQuery(sqlString, Token.class);

        for (Entry<String, Object> attribute : attributes.entrySet()) {
            query.setParameter(attribute.getKey(), attribute.getValue());
        }

        return (List<Token>)query.getResultList();
    }
}
