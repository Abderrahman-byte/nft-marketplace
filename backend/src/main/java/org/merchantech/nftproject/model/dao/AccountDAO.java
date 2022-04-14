package org.merchantech.nftproject.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.merchantech.nftproject.model.bo.Account;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDAO {
    @PersistenceContext
    EntityManager entityManager;

    public Account getAccountByUsername (String username) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> cq = cb.createQuery(Account.class);
        Root<Account> root = cq.from(Account.class);
        Query query;

        cq.select(root).where(cb.equal(root.get("username"), username));

        query = entityManager.createQuery(cq);

        try {
            return (Account)query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
