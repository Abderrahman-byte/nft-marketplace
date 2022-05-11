package org.stibits.rnft.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.Bid;
import org.stibits.rnft.entities.Token;
import org.stibits.rnft.errors.AccountNotFound;
import org.stibits.rnft.errors.NotFoundError;
import org.stibits.rnft.errors.TokenNotFound;
import org.stibits.rnft.utils.RandomGenerator;

@Repository
public class BidsDAO {
    @Autowired
    private RandomGenerator randomGenerator;

    @PersistenceContext
    private EntityManager entityManager;

    // TODO : this query must be optimised
    @Transactional
    public Bid insertBid (Account from, Account to, Token token, double price) {
        Bid bid = new Bid();

        bid.setId(randomGenerator.generateRandomStr(25));
        bid.setFrom(from);
        bid.setTo(to);
        bid.setPrice(price);
        bid.setToken(token);

        entityManager.persist(bid);

        return bid;
    }

    
    @Transactional
    public Bid insertBid (Account from, String to, String tokenId, double price) throws NotFoundError {
        Account toAccount = entityManager.find(Account.class, to);
        Token token = entityManager.find(Token.class, tokenId);

        if (toAccount == null) throw new AccountNotFound();
        if (token == null) throw new TokenNotFound();

        return this.insertBid(from, toAccount, token, price);
    }

    @SuppressWarnings("unchecked")
    public List<Bid> selectBidsByTokenId (String id, int limit, int offset) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Bid> cq = cb.createQuery(Bid.class);
        Root<Bid> root = cq.from(Bid.class);

        cq.select(root).where(
            cb.equal(root.get("token").get("id"), id)
        ).orderBy(
            cb.desc(root.get("createdDate"))
        );

        Query query = this.entityManager.createQuery(cq);
        query.setFirstResult(offset);
        query.setMaxResults(limit);


        return (List<Bid>)query.getResultList();
    }

    public Bid getTokenHighestBid (String id) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Bid> cq = cb.createQuery(Bid.class);
        Root<Bid> root = cq.from(Bid.class);
        
        cq.select(root).where(
            cb.equal(root.get("token").get("id"), id)
        ).orderBy(
            cb.desc(root.get("price"))  
        );

        Query query = this.entityManager.createQuery(cq);

        query.setMaxResults(1);
        
        try {
            return (Bid)query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
