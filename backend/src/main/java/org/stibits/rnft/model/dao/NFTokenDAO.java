package org.stibits.rnft.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.stibits.rnft.model.bo.Account;
import org.stibits.rnft.model.bo.NFToken;
import org.stibits.rnft.utils.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class NFTokenDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RandomGenerator randomGenerator;

    public NFToken insertNFT (Account creator, String title, String description, String contentUrl, double price) {
        return this.insertNFT(creator, title, description, contentUrl, true, price);
    }

    public NFToken insertNFT (Account creator, String title, String description, String contentUrl) {
        return this.insertNFT(creator, title, description, contentUrl, false, 0);
    }

    @Transactional
    public NFToken insertNFT (Account creator, String title, String description, String contentUrl, boolean forSell, double price) {
        NFToken nft = new NFToken();

        nft.setId(randomGenerator.generateRandomStr(25));

        nft.setCreator(creator);
        nft.setOwner(creator);

        nft.setTitle(title);
        nft.setDescription(description);
        nft.setPreviewUrl(contentUrl);
        nft.setForSell(forSell);
        nft.setPrice(price);

        return entityManager.merge(nft);
    }
}