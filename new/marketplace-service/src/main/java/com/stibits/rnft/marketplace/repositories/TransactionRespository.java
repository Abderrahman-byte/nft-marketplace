package com.stibits.rnft.marketplace.repositories;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stibits.rnft.marketplace.domain.Token;
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

    @Transactional
    public Transaction insertTransaction(Token token, String accountFrom, String accountTo, double price) {
        Transaction transaction = new Transaction();

        transaction.setToken(token);
        transaction.setFromId(accountFrom);
        transaction.setToId(accountTo);
        transaction.setPrice(price);

        return entityManager.merge(transaction);
    }

    public List<Transaction> selectTransactionsByTokenId(String id) {
        CriteriaQuery<Transaction> cq = this.criteriaBuilder.createQuery(Transaction.class);
        Root<Transaction> root = cq.from(Transaction.class);

        cq.select(root).where(
                this.criteriaBuilder.equal(root.get("token").get("id"), id)).orderBy(
                        this.criteriaBuilder.desc(root.get("createdDate")));

        return this.entityManager.createQuery(cq).getResultList();
    }

    public double getTransactionsSumOfAccount(String id) {
        String sqlString = "select COALESCE(SUM(transaction.price), 0) as total " +
                "from transaction where from_id = :id";

        Query query = this.entityManager.createNativeQuery(sqlString);
        query.setParameter("id", id);

        try {
            return (Double) query.getSingleResult();
        } catch (Exception ex) {
            return 0;
        }
    }
}
