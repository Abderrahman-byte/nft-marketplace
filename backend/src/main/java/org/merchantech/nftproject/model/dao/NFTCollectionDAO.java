package org.merchantech.nftproject.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.merchantech.nftproject.model.bo.Account;
import org.merchantech.nftproject.model.bo.NftCollection;
import org.merchantech.nftproject.utils.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class NFTCollectionDAO {


		@PersistenceContext
	    private EntityManager entityManager;
		@Autowired
	    private RandomGenerator randomGenerator;

	    @Transactional
	    public NftCollection insertNFTCollection (String name, String description, Account acc) {
	       NftCollection nftcollection = new NftCollection();
	      
	       nftcollection.setName(name);
	       nftcollection.setDescription(description);
	       nftcollection.setAccount(acc);
	       nftcollection.setId(randomGenerator.generateRandomStr(25));
	       entityManager.persist(nftcollection);

	        return nftcollection;
	    }

}
