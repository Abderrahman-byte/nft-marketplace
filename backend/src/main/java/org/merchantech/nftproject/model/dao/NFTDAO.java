package org.merchantech.nftproject.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.merchantech.nftproject.model.bo.Account;
import org.merchantech.nftproject.model.bo.NftToken;
import org.merchantech.nftproject.utils.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class NFTDAO {
	@PersistenceContext
    private EntityManager entityManager;

	@Autowired
    private RandomGenerator randomGenerator;

    @Transactional
    public NftToken insertNFT (String title, Double price, String preview_url, boolean is_for_sell, Account acc) {
       NftToken nft = new NftToken();
       
       nft.setTitle(title);
       nft.setCreator(acc);
       nft.setOwner(acc);
       nft.setPrice(price);
       nft.setPreviewUrl(preview_url);
       nft.setForSell(is_for_sell);
       nft.setId(randomGenerator.generateRandomStr(25));

       entityManager.persist(nft);

        return nft;
    }
}
