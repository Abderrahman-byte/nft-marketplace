package com.stibits.rnft.marketplace.repositories;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.stibits.rnft.marketplace.domain.Bid;

@Repository
public class BidRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaBuilder criteriaBuilder;

    @PostConstruct
    public void getCriteriaBuilder () {
        this.criteriaBuilder = this.entityManager.getCriteriaBuilder();
    }

    public List<Bid> getBidsOfToken (String tokenId, int limit, int offset) {
        CriteriaQuery<Bid> criteria = this.criteriaBuilder.createQuery(Bid.class);
        Root<Bid> bid = criteria.from(Bid.class);

        criteria.select(bid).where(
            this.criteriaBuilder.equal(bid.get("token").get("id"), tokenId)
        ).orderBy(
            this.criteriaBuilder.desc(bid.get("createdDate"))
        );

        return this.entityManager.createQuery(criteria).setMaxResults(limit).setFirstResult(offset).getResultList();
    }
}
