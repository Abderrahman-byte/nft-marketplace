package org.merchantech.nftproject.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.merchantech.nftproject.model.bo.Account;
import org.merchantech.nftproject.model.bo.Session;
import org.merchantech.nftproject.utils.PasswordHasher;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SessionDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
    public void insertSession (Session s) {
        
        entityManager.persist(s);
    }
}
