package org.merchantech.nftproject.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.merchantech.nftproject.model.bo.Account;
import org.merchantech.nftproject.model.bo.NFT_Token;
import org.merchantech.nftproject.utils.PasswordHasher;
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
    public NFT_Token insertNFT (String title, Double price, String preview_url, String files_zip_url, boolean is_for_sell, Account acc) {
       NFT_Token nft = new NFT_Token();
       
       nft.setTitle(title);
       nft.setAccount(acc);
       nft.setOwner(acc);
       nft.setPrice(price);
       nft.setPreview_url(preview_url);
       nft.setFiles_zip_url(files_zip_url);
       nft.setIs_for_sell(is_for_sell);
       nft.setId(randomGenerator.generateRandomStr(25));
       entityManager.persist(nft);

        return nft;
    }


}
