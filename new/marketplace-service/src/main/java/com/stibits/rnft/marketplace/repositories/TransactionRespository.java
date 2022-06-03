package com.stibits.rnft.marketplace.repositories;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stibits.rnft.marketplace.domain.Transaction;

@Repository
public class TransactionRespository {
    @Autowired
    private EntityManager entityManager;

    private CriteriaBuilder criteriaBuilder;

    @PostConstruct
    public void getCriteriaBuilder () {
        this.criteriaBuilder = this.entityManager.getCriteriaBuilder();
    }

    public List<Transaction> getTransactionsOfToken (String id, int limit, int offset) {
        CriteriaQuery<Transaction> criteria = this.criteriaBuilder.createQuery(Transaction.class);
        Root<Transaction> transaction = criteria.from(Transaction.class);

        criteria.select(transaction).where(
            this.criteriaBuilder.equal(transaction.get("token").get("id"), id)
        ).orderBy(
            this.criteriaBuilder.desc(transaction.get("createdDate"))
        );

        return this.entityManager.createQuery(criteria).setMaxResults(limit).setFirstResult(offset).getResultList();
    }
}
