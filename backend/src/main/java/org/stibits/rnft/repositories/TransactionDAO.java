package org.stibits.rnft.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.Token;
import org.stibits.rnft.entities.Transaction;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.NotFoundError;
import org.stibits.rnft.errors.TokenNotFound;
import org.stibits.rnft.utils.RandomGenerator;

@Repository
public class TransactionDAO {
	@Autowired
	private RandomGenerator randomGenerator;

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public Transaction insertTransaction(String tokenId, String accountFrom, String accountTo, double price) throws ApiError {
		Token token = this.entityManager.find(Token.class, tokenId);
		Account from = this.entityManager.find(Account.class, accountFrom);
		Account to = this.entityManager.find(Account.class, accountTo);

		if (token == null) throw new TokenNotFound();

		if (from == null || to == null) throw new NotFoundError();

		return this.insertTransaction(token, from, to, price);
	}

	@Transactional
	public Transaction insertTransaction(Token token, Account accountFrom, Account accountTo, double price) {
		Transaction trans = new Transaction();

		trans.setId(randomGenerator.generateRandomStr(25));
		trans.setToken(token);
		trans.setFrom(accountFrom);
		trans.setTo(accountTo);
		trans.setPrice(price);

		return entityManager.merge(trans);
	}

	public int getAccountTokenBalance (Token token, Account account) {
		return this.getAccountTokenBalance(token, account.getId());
	}
	
	// TODO : check token quantity
	public int getAccountTokenBalance (Token token, String accountId) {
		int balance = accountId.equals(token.getCreator().getId()) ? 1 : 0;

		List<Transaction> transactions = token.getTransaction().stream().sorted((a, b) -> {
				return a.getCreatedDate().compareTo(b.getCreatedDate());
			}).toList();

		for (Transaction transaction : transactions) {
			if (transaction.getFrom().getId().equals(accountId)) balance--;
			if (transaction.getTo().getId().equals(accountId)) balance++;
		}

		return balance;
	}

	@Transactional
	public Account getTokenOwner (String id) {
		Token token = this.entityManager.find(Token.class, id);
		
		if (token == null) return null;

		return this.getTokenOwner(token);
	}

	@Transactional
	public Account getTokenOwner (Token token) {
		List<Transaction> transactions = token.getTransaction().stream().sorted(
			(a, b) -> a.getCreatedDate().compareTo(b.getCreatedDate())
		).toList();

		if (transactions.isEmpty()) return token.getCreator();

		return transactions.get(transactions.size() - 1).getTo();
	}
}
