package org.stibits.rnft.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
}
