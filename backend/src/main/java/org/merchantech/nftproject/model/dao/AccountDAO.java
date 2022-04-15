package org.merchantech.nftproject.model.dao;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.merchantech.nftproject.model.bo.Account;
import org.merchantech.nftproject.utils.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.merchantech.nftproject.utils.RandomGenerator;

@Repository
public class AccountDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RandomGenerator randomGenerator;

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

    
    @Transactional
    public Account insertAccount (Map<String, Object> data) {
        return this.insertAccount(
            (String) data.get("username"),
            (String) data.get("email"), 
            (String) data.get("password"),
            (Boolean) data.get("isArtist")
        );
    }

    @Transactional
    public Account insertAccount (String username, String email, String password, boolean isArtist) {
        Account account = new Account();

        account.setUsername(username);
        account.setArtist(isArtist);
        account.setEmail(email);
        account.setPassword(PasswordHasher.hashPassword(password));
        account.setId(randomGenerator.generateRandomStr(25));

        entityManager.persist(account);

        return account;
    }

}
