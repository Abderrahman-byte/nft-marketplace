package com.stibits.rnft.gateway.repository;

import javax.persistence.Query;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.stibits.rnft.gateway.domain.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account getAccountById (String id) {
        return entityManager.find(Account.class, id);
    }

    public Account getAccountByUsername (String username) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> cq = cb.createQuery(Account.class);
        Root<Account> root = cq.from(Account.class);

        cq.select(root).where(
            cb.or(
                cb.equal(root.get("username"), username), 
                cb.equal(root.get("email"), username)
            )
        );

        try {
            return entityManager.createQuery(cq).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Transactional
    public Account insertAccount (String username, String email, String password) {
        Account account = new Account();

        account.setUsername(username);
        account.setEmail(email);
        account.setPassword(this.passwordEncoder.encode(password));
        account.getProfile().setDisplayName(username);

        entityManager.persist(account);

        return account;
    }

    @Transactional
    public boolean setAccountAsVerified (String accountId) {
        Query query = entityManager.createQuery("Update Account a set a.verified = true where a.id = :id");
        query.setParameter("id", accountId);

        return query.executeUpdate() > 0;
    }

    @Transactional
    public boolean activateAccount (String accountId) {
        Query query = entityManager.createQuery("Update Account a set a.active = true where a.id = :id");
        query.setParameter("id", accountId);

        return query.executeUpdate() > 0;
    }

    @SuppressWarnings("unchecked")
    public List<Account> searchForAccount (String query, int limit, int offset) {
        String qlString = "select a.id, a.username, a.email, a.is_verified, a.is_active, a.created_date, a.updated_date, a.is_admin, a.password " +
        "from account a join account_profile p on a.id = p.account_id " +
        "where (a.username REGEXP :query OR p.display_name REGEXP :query) AND a.is_active = true " +
        "limit :limit offset :offset";

        Query sqlQuery = this.entityManager.createNativeQuery(qlString, Account.class);

        sqlQuery.setParameter("query", query);
        sqlQuery.setParameter("limit", limit);
        sqlQuery.setParameter("offset", offset);

        return (List<Account>)sqlQuery.getResultList();
    }
}
