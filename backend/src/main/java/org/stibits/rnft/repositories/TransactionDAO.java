package org.stibits.rnft.repositories;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.Transaction;



@Repository
public class TransactionDAO {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public Transaction insertTransaction( String tokenid, String accountFrom, String accountTo, double price) {
		Transaction trans = new Transaction();
		  trans.setTokenid(tokenid);
		  trans.setAccountFrom(accountFrom);
		  trans.setAccountTo(accountTo);
		  trans.setPrice(price);
		  
		return entityManager.merge(trans);	
	}
	
	

}
