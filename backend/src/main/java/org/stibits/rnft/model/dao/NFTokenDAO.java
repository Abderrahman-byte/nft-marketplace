package org.stibits.rnft.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.stibits.rnft.model.bo.Account;
import org.stibits.rnft.model.bo.NFToken;
import org.stibits.rnft.model.bo.NftCollection;
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
    public List<NFToken> insertMultipleNFT (Account account, NftCollection collection, Map<String, Object> data, String contentUrl) {
        int quantity = (Integer)data.get("quantity");
        List<NFToken> nfts = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            Map<String, Object> nftData = new HashMap<>(data);
            nftData.put("title", data.get("title") + " #" + String.format("%05d", randomGenerator.randomInteger(10, 99999)));
            nfts.add(this.insertNFT(account, collection, nftData, contentUrl));
        }

        return nfts;
    }

    @Transactional
    public NFToken insertNFT (Account creator, NftCollection collection, Map<String, Object> data, String contentUrl) {
        Boolean isForSell = (Boolean)data.get("isForSell");

        if (isForSell)
            return this.insertNFT(creator, collection, (String)data.get("title"), (String)data.get("title"), contentUrl, (Double)data.get("price"));

        return this.insertNFT(creator, collection, (String)data.get("title"), (String)data.get("title"), contentUrl);
    }

    public NFToken insertNFT (Account creator, NftCollection collection, String title, String description, String contentUrl, double price) {
        return this.insertNFT(creator, collection, title, description, contentUrl, true, price);
    }

    public NFToken insertNFT (Account creator, NftCollection collection, String title, String description, String contentUrl) {
        return this.insertNFT(creator, collection, title, description, contentUrl, false, 0);
    }

    @Transactional
    public NFToken insertNFT (Account creator, NftCollection collection, String title, String description, String contentUrl, boolean forSell, double price) {
        NFToken nft = new NFToken();

        nft.setId(randomGenerator.generateRandomStr(25));

        nft.setCreator(creator);
        nft.setOwner(creator);

        if (collection != null) nft.setCollection(collection);

        nft.setTitle(title);
        nft.setDescription(description);
        nft.setPreviewUrl(contentUrl);
        nft.setForSell(forSell);
        nft.setPrice(price);

        return entityManager.merge(nft);
    }

    public List<NFToken> selectTokensSortedByLikes (int limit, int offset, double maxPrice) {
        return this.selectToken("order by likes DESC, created_date DESC", limit, offset, maxPrice);
    }

    public List<NFToken> selectTokensSortedByHighPrice (int limit, int offset, double maxPrice) {
        return this.selectToken("order by price DESC, created_date DESC", limit, offset, maxPrice);
    }

    public List<NFToken> selectTokensSortedByLowPrice (int limit, int offset, double maxPrice) {
        return this.selectToken("order by price ASC, created_date DESC", limit, offset, maxPrice);
    }

    public List<NFToken> selectTokensSortedByLikes (int limit, int offset) {
        return this.selectToken("order by likes DESC, created_date DESC", limit, offset);
    }

    public List<NFToken> selectTokensSortedByHighPrice (int limit, int offset) {
        return this.selectToken("order by price DESC, created_date DESC", limit, offset);
    }

    public List<NFToken> selectTokensSortedByLowPrice (int limit, int offset) {
        return this.selectToken("order by price ASC, created_date DESC", limit, offset);
    }

    public List<NFToken> selectTokensSortedByLikes (String collectionId,int limit, int offset) {
        return this.selectToken("order by likes DESC, created_date DESC", collectionId, limit, offset);
    }

    public List<NFToken> selectTokensSortedByHighPrice (String collectionId, int limit, int offset) {
        return this.selectToken("order by price DESC, created_date DESC", collectionId, limit, offset);
    }

    public List<NFToken> selectTokensSortedByLowPrice (String collectionId, int limit, int offset) {
        return this.selectToken("order by price ASC, created_date DESC", collectionId, limit, offset);
    }

    public List<NFToken> selectTokensSortedByLikes (String collectionId,int limit, int offset, double maxPrice) {
        return this.selectToken("order by likes DESC, created_date DESC", collectionId, limit, offset, maxPrice);
    }

    public List<NFToken> selectTokensSortedByHighPrice (String collectionId, int limit, int offset, double maxPrice) {
        return this.selectToken("order by price DESC, created_date DESC", collectionId, limit, offset, maxPrice);
    }

    public List<NFToken> selectTokensSortedByLowPrice (String collectionId, int limit, int offset, double maxPrice) {
        return this.selectToken("order by price ASC, created_date DESC", collectionId, limit, offset, maxPrice);
    }

    public NFToken selectToken (String id) {
        String qlString = "Select t from NFToken t where id = :id or title = :id";
        Query query = entityManager.createQuery(qlString, NFToken.class);
        query.setParameter("id", id);

        try {
            return (NFToken)query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Transactional
    public boolean likeItem (String accountId, String tokenId) {
        String qlString = "INSERT INTO nft_token_likes (token_id, account_id) VALUES (?, ?)";
        Query query = entityManager.createNativeQuery(qlString);

        query.setParameter(1, tokenId);
        query.setParameter(2, accountId);

        return query.executeUpdate() > 0;
    }

    @Transactional
    public boolean unlikeItem (String accountId, String tokenId) {
        String qlString = "Delete from NftLike Where accountId = :accountId and tokenId = :tokenId";
        Query query = entityManager.createQuery(qlString);

        query.setParameter("accountId", accountId);
        query.setParameter("tokenId", tokenId);

        return query.executeUpdate() > 0;
    }

    // FIXME : this maybe unsafe, re-check later
    @SuppressWarnings("unchecked")
    private List<NFToken> selectToken (String addSql, int limit, int offset) {
        String sqlString = "select id, title, preview_url, price, artist_id,owner_id, description,is_for_sell,collection_id, created_date, " +
            "(select count(account_id) as likes from nft_token_likes where nft_token_likes.token_id = nft_token.id ) as likes "+ 
            "from nft_token " + addSql + " limit :limit offset :offset";

        Query query = entityManager.createNativeQuery(sqlString, NFToken.class);
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);

        
        List<NFToken> list = (List<NFToken>)query.getResultList();

        return list;
    }

    // FIXME : this maybe unsafe, re-check later
    @SuppressWarnings("unchecked")
    private List<NFToken> selectToken (String addSql, int limit, int offset, double maxPrice) {
        String sqlString = "select id, title, preview_url, price, artist_id,owner_id, description,is_for_sell,collection_id, created_date, " +
            "(select count(account_id) as likes from nft_token_likes where nft_token_likes.token_id = nft_token.id ) as likes "+ 
            "from nft_token where price <= :max " + addSql + " limit :limit offset :offset";

        Query query = entityManager.createNativeQuery(sqlString, NFToken.class);
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);
        query.setParameter("max", maxPrice);
        
        List<NFToken> list = (List<NFToken>)query.getResultList();

        return list;
    }

    // FIXME : this maybe unsafe, re-check later
    @SuppressWarnings("unchecked")
    private List<NFToken> selectToken (String addSql, String collectionId, int limit, int offset) {
        String sqlString = "select id, title, preview_url, price, artist_id,owner_id, description,is_for_sell,collection_id, created_date, " +
            "(select count(account_id) as likes from nft_token_likes where nft_token_likes.token_id = nft_token.id ) as likes "+ 
            "from nft_token where collection_id = :collectionId " + addSql + " limit :limit offset :offset";

        Query query = entityManager.createNativeQuery(sqlString, NFToken.class);
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);
        query.setParameter("collectionId", collectionId);

        List<NFToken> list = (List<NFToken>)query.getResultList();

        return list;
    }

    // FIXME : this maybe unsafe re-check 
    @SuppressWarnings("unchecked")
    private List<NFToken> selectToken (String addSql, String collectionId, int limit, int offset, double maxPrice) {
        String sqlString = "select id, title, preview_url, price, artist_id,owner_id, description,is_for_sell,collection_id, created_date, " +
            "(select count(account_id) as likes from nft_token_likes where nft_token_likes.token_id = nft_token.id ) as likes "+ 
            "from nft_token where collection_id = :collectionId and price <= :max " + addSql + " limit :limit offset :offset";

        Query query = entityManager.createNativeQuery(sqlString, NFToken.class);
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);
        query.setParameter("max", maxPrice);
        query.setParameter("collectionId", collectionId);

        List<NFToken> list = (List<NFToken>)query.getResultList();

        return list;
    }
}
