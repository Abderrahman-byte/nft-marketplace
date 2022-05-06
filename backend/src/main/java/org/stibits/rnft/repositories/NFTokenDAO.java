package org.stibits.rnft.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.Token;
import org.stibits.rnft.entities.NftCollection;
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
    public List<Token> insertMultipleNFT (Account account, NftCollection collection, Map<String, Object> data, String contentUrl) {
        int quantity = (Integer)data.get("quantity");
        List<Token> nfts = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            Map<String, Object> nftData = new HashMap<>(data);
            nftData.put("title", data.get("title") + " #" + String.format("%05d", randomGenerator.randomInteger(10, 99999)));
            nfts.add(this.insertNFT(account, collection, nftData, contentUrl));
        }

        return nfts;
    }

    @Transactional
    public Token insertNFT (Account creator, NftCollection collection, Map<String, Object> data, String contentUrl) {
        boolean instantSale = data.containsKey("instantSale") ? (Boolean)data.get("instantSale") : false;
        boolean isforSale = data.containsKey("instantSale") ? (Boolean)data.get("isForSale") : instantSale;
        double price = data.containsKey("price") ? (Double)data.get("price") : 0;

        return this.insertNFT(
            creator, 
            collection, 
            (String)data.get("title"), 
            (String)data.get("description"), 
            contentUrl, 
            isforSale, 
            instantSale, 
            price
        );
    }

    @Transactional
    public Token insertNFT (Account creator, NftCollection collection, String title, String description, String contentUrl, boolean forSale, boolean instantSale, double price) {
        Token nft = new Token();

        nft.setId(randomGenerator.generateRandomStr(25));

        nft.setCreator(creator);
        nft.setOwner(creator);

        if (collection != null) nft.setCollection(collection);
        if (price > 0) nft.getSettings().setPrice(price);

        nft.setTitle(title);
        nft.setDescription(description);
        nft.setPreviewUrl(contentUrl);
        nft.getSettings().setForSale(forSale);
        nft.getSettings().setInstantSale(instantSale);

        entityManager.persist(nft.getSettings());

        return entityManager.merge(nft);
    }

    public List<Token> selectTokensSortedByLikes (int limit, int offset, double maxPrice) {
        return this.selectToken("order by likes DESC, created_date DESC", limit, offset, maxPrice);
    }

    public List<Token> selectTokensSortedByHighPrice (int limit, int offset, double maxPrice) {
        return this.selectToken("order by price DESC, created_date DESC", limit, offset, maxPrice);
    }

    public List<Token> selectTokensSortedByLowPrice (int limit, int offset, double maxPrice) {
        return this.selectToken("order by price ASC, created_date DESC", limit, offset, maxPrice);
    }

    public List<Token> selectTokensSortedByLikes (int limit, int offset) {
        return this.selectToken("order by likes DESC, created_date DESC", limit, offset);
    }

    public List<Token> selectTokensSortedByHighPrice (int limit, int offset) {
        return this.selectToken("order by price DESC, created_date DESC", limit, offset);
    }

    public List<Token> selectTokensSortedByLowPrice (int limit, int offset) {
        return this.selectToken("order by price ASC, created_date DESC", limit, offset);
    }

    public List<Token> selectTokensSortedByLikes (String collectionId,int limit, int offset) {
        return this.selectToken("order by likes DESC, created_date DESC", collectionId, limit, offset);
    }

    public List<Token> selectTokensSortedByHighPrice (String collectionId, int limit, int offset) {
        return this.selectToken("order by price DESC, created_date DESC", collectionId, limit, offset);
    }

    public List<Token> selectTokensSortedByLowPrice (String collectionId, int limit, int offset) {
        return this.selectToken("order by price ASC, created_date DESC", collectionId, limit, offset);
    }

    public List<Token> selectTokensSortedByLikes (String collectionId,int limit, int offset, double maxPrice) {
        return this.selectToken("order by likes DESC, created_date DESC", collectionId, limit, offset, maxPrice);
    }

    public List<Token> selectTokensSortedByHighPrice (String collectionId, int limit, int offset, double maxPrice) {
        return this.selectToken("order by price DESC, created_date DESC", collectionId, limit, offset, maxPrice);
    }

    public List<Token> selectTokensSortedByLowPrice (String collectionId, int limit, int offset, double maxPrice) {
        return this.selectToken("order by price ASC, created_date DESC", collectionId, limit, offset, maxPrice);
    }

    public Token selectToken (String id) {
        String qlString = "Select t from Token t where id = :id or title = :id";
        Query query = entityManager.createQuery(qlString, Token.class);
        query.setParameter("id", id);

        try {
            Token token = (Token)query.getSingleResult();
            token.setOwner(token.getCreator());
            return token;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Transactional
    public boolean likeItem (String accountId, String tokenId) {
        String qlString = "INSERT INTO token_likes (token_id, account_id) VALUES (?, ?)";
        Query query = entityManager.createNativeQuery(qlString);

        query.setParameter(1, tokenId);
        query.setParameter(2, accountId);

        return query.executeUpdate() > 0;
    }

    @Transactional
    public boolean unlikeItem (String accountId, String tokenId) {
        String qlString = "Delete from TokenLike Where accountId = :accountId and tokenId = :tokenId";
        Query query = entityManager.createQuery(qlString);

        query.setParameter("accountId", accountId);
        query.setParameter("tokenId", tokenId);

        return query.executeUpdate() > 0;
    }

    public List<Token> getTokensOwnedBy (String accountId, int limit, int offset) {
        return List.of();
    }
    
    public List<Token> getTokensForSaleBy (String accountId, int limit, int offset) {
        return List.of();
    }

    @SuppressWarnings("unchecked")
    public List<Token> getUserFavoriteTokens (String accountId, int limit, int offset) {
        String sqlString = "select id, title, preview_url, artist_id, description, collection_id, created_date "+
        "from token "+
        "where id in (select token_id from token_likes where account_id = :accountId) "+
        "order by created_date DESC, id ASC limit :limit offset :offset";

        Query query = entityManager.createNativeQuery(sqlString, Token.class);
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);
        query.setParameter("accountId", accountId);    
            
        List<Token> tokenslist = (List<Token>)query.getResultList();

        tokenslist.forEach(token -> token.setOwner(token.getCreator()));
    
        return tokenslist;        
    }

    @SuppressWarnings("unchecked")
    public List<Token> getTokensCreatedBy (String accountId, int limit, int offset) {
        String sqlString = "select id, title, preview_url, artist_id, description,collection_id, created_date " +
            "from token where artist_id = :accountId order by created_date DESC limit :limit offset :offset";

        Query query = entityManager.createNativeQuery(sqlString, Token.class);
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);
        query.setParameter("accountId", accountId);    
            
        List<Token> tokenslist = (List<Token>)query.getResultList();

        tokenslist.forEach(token -> token.setOwner(token.getCreator()));
    
        return tokenslist;
    }

    // FIXME : this maybe unsafe, re-check later
    @SuppressWarnings("unchecked")
    private List<Token> selectToken (String addSql, int limit, int offset) {
        String sqlString = "select id, title, preview_url,artist_id, description,collection_id, created_date, " +
            "(select count(account_id) as likes from token_likes where token_likes.token_id = token.id ) as likes "+ 
            "from token " + addSql + " limit :limit offset :offset";

        Query query = entityManager.createNativeQuery(sqlString, Token.class);
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);

        
        List<Token> tokenslist = (List<Token>)query.getResultList();

        tokenslist.forEach(token -> token.setOwner(token.getCreator()));
    
        return tokenslist;
    }

    // FIXME : this maybe unsafe, re-check later
    @SuppressWarnings("unchecked")
    private List<Token> selectToken (String addSql, int limit, int offset, double maxPrice) {
        String sqlString = "select token.id as id, token.title as title, token.preview_url as preview_url, token.artist_id as artist_id, "+
            "token.description as description, token.collection_id, token.created_date, settings.price as price, " +
            "(select count(account_id) as likes from token_likes where token_likes.token_id = token.id ) as likes " +
            "from token " +
            "JOIN token_settings AS settings ON settings.token_id = token.id " +
            "where price <= :max " + addSql + " limit :limit offset :offset";

        Query query = entityManager.createNativeQuery(sqlString, Token.class);
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);
        query.setParameter("max", maxPrice);
        
        List<Token> tokenslist = (List<Token>)query.getResultList();

        tokenslist.forEach(token -> token.setOwner(token.getCreator()));
    
        return tokenslist;
    }

    // FIXME : this maybe unsafe, re-check later
    @SuppressWarnings("unchecked")
    private List<Token> selectToken (String addSql, String collectionId, int limit, int offset) {
        String sqlString = "select id, title, preview_url, artist_id, description,collection_id, created_date, " +
            "(select count(account_id) as likes from token_likes where token_likes.token_id = token.id ) as likes " +
            "from token where collection_id = :collectionId " + addSql + " limit :limit offset :offset";

        Query query = entityManager.createNativeQuery(sqlString, Token.class);
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);
        query.setParameter("collectionId", collectionId);

        List<Token> tokenslist = (List<Token>)query.getResultList();

        tokenslist.forEach(token -> token.setOwner(token.getCreator()));
    
        return tokenslist;
    }

    // FIXME : this maybe unsafe re-check 
    @SuppressWarnings("unchecked")
    private List<Token> selectToken (String addSql, String collectionId, int limit, int offset, double maxPrice) {
        String sqlString = "select id, title, preview_url, artist_id, description,collection_id, created_date " +
            "from token where collection_id = :collectionId and price <= :max " + addSql + " limit :limit offset :offset";

        Query query = entityManager.createNativeQuery(sqlString, Token.class);
        query.setParameter("offset", offset);
        query.setParameter("limit", limit);
        query.setParameter("max", maxPrice);
        query.setParameter("collectionId", collectionId);

        List<Token> tokenslist = (List<Token>)query.getResultList();

        tokenslist.forEach(token -> token.setOwner(token.getCreator()));
    
        return tokenslist;
    }
}
