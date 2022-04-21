package org.merchantech.nftproject.model.dao;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.merchantech.nftproject.model.bo.Account;
import org.merchantech.nftproject.model.bo.NFToken;
import org.merchantech.nftproject.utils.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class NFTokenDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RandomGenerator randomGenerator;

    @Transactional
    public NFToken insertNFT (Account creator, Map<String, Object> data, String contentUrl) {
        Boolean isForSell = (Boolean)data.get("isForSell");

        if (isForSell) {
            return this.insertNFT(creator, (String)data.get("title"), (String)data.get("title"), contentUrl, (Double)data.get("price"));
        }

        return this.insertNFT(creator, (String)data.get("title"), (String)data.get("title"), contentUrl);
    }

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
