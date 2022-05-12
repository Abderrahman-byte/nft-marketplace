package org.stibits.rnft.repositories;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.stibits.rnft.entities.Account;
import org.stibits.rnft.utils.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.stibits.rnft.utils.RandomGenerator;

@Repository
public class AccountDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RandomGenerator randomGenerator;

    public Account getAccountById (String id) {
        return entityManager.find(Account.class, id);
    }

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
            (String) data.get("password")
        );
    }

    @Transactional
    public Account insertAccount (String username, String email, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setEmail(email);
        account.setPassword(PasswordHasher.hashPassword(password));
        account.setId(randomGenerator.generateRandomStr(25));

        entityManager.persist(account);

        return account;
    }

    @Transactional
    public boolean setAccountAsVerified (String accountId) {
        Query query = entityManager.createQuery("Update Account a set a.isVerified = true where a.id = :id");
        query.setParameter("id", accountId);

        return query.executeUpdate() > 0;
    }

    @SuppressWarnings("unchecked")
    public List<Account> getBestSellers (int limit) {
        String sqlString = "select a.id, a.username, a.email, a.password, a.is_admin, a.is_verified, a.created_date, a.updated_date, " +
        "COUNT(t.to_account) as transactions_count, SUM(t.price) as transactions_sum " +
        "from account as a "+ 
        "join transactions as t on t.from_account = a.id and t.created_date >= DATE_SUB(NOW(), INTERVAL 1 MONTH) "+
        "group by a.id, a.username " +
        "order by transactions_count DESC, transactions_sum DESC limit :limit";

        Query query = this.entityManager.createNativeQuery(sqlString, Account.class);
        query.setParameter("limit", limit);
        
        return (List<Account>)query.getResultList();
    }
}
