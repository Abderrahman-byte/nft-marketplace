package org.stibits.rnft.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Transactional
    public List<NFToken> insertMultipleNFT (Account account, Map<String, Object> data, String contentUrl) {
        int quantity = (Integer)data.get("quantity");
        List<NFToken> nfts = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            Map<String, Object> nftData = new HashMap<>(data);
            nftData.put("title", data.get("title") + " #" + String.format("%05d", randomGenerator.randomInteger(10, 99999)));
            nfts.add(this.insertNFT(account, nftData, contentUrl));
        }

        return nfts;
    }

    @Transactional
    public NFToken insertNFT (Account creator, Map<String, Object> data, String contentUrl) {
        Boolean isForSell = (Boolean)data.get("isForSell");

        if (isForSell)
            return this.insertNFT(creator, (String)data.get("title"), (String)data.get("title"), contentUrl, (Double)data.get("price"));

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
