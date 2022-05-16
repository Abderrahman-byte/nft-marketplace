package org.stibits.rnft.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.Token;
import org.stibits.rnft.entities.TokenSettings;
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

    @Autowired
    private TransactionDAO transactionDAO;

    public Token selectTokenById (String id) {
        Token token = this.entityManager.find(Token.class, id);
        
        if (token != null) token.setOwner(transactionDAO.getTokenOwner(token));

        return token;
    }

    @Transactional
    public TokenSettings updateTokenSettings (Token token, Map<String,Object> data) {
        boolean isForSale = data.containsKey("isForSale") ? (Boolean)data.get("isForSale") : token.getSettings().isForSale();
        boolean instantSale = data.containsKey("instantSale") ? (Boolean)data.get("instantSale") && isForSale : token.getSettings().isInstantSale();
        double price = data.containsKey("price") ? (Double)data.get("price") : token.getSettings().getPrice();

        return this.updateTokenSettings(token, isForSale, instantSale, price);
    }

    @Transactional
    public TokenSettings updateTokenSettings (String tokenId, double price) {
        Token token = this.entityManager.find(Token.class, tokenId);

        if (token == null) return null;

        return this.updateTokenSettings(token, price);
    }

    @Transactional
    public TokenSettings updateTokenSettings (String tokenId, boolean isForSale, boolean instantSale, double price) {
        Token token = this.entityManager.find(Token.class, tokenId);

        if (token == null) return null;

        return this.updateTokenSettings(token, isForSale, instantSale, price);
    }

    @Transactional
    public TokenSettings updateTokenSettings (Token token, double price) {
        return this.updateTokenSettings(token, true, false, price);
    }

    @Transactional
    public TokenSettings updateTokenSettings (Token token, boolean isForSale, boolean instantSale, double price) {
        TokenSettings settings = token.getSettings();

        settings.setForSale(isForSale);
        settings.setInstantSale(instantSale);
        settings.setPrice(price);

        return this.entityManager.merge(settings);
    }

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

    @SuppressWarnings("unchecked")
    public List<Token> selectTokensSortedByPopularity (int limit, int offset, double maxPrice) {
        String sqlString = "select t.id, t.title, t.artist_id, t.collection_id, t.preview_url, t.description, t.created_date, " +
        "(select count(*) from bids where token_id = t.id and created_date >= DATE_SUB(NOW(), INTERVAL :interval DAY)) as bids_count, "+
        "(select count(*) from token_likes where token_id = t.id and created_date >= DATE_SUB(NOW(), INTERVAL :interval DAY)) as likes_count "+
        "from token as t "+
        "join token_settings as settings on settings.token_id = t.id and settings.price <= :maxPrice " +
        "order by bids_count DESC, likes_count DESC, t.created_date DESC "+
        "LIMIT :limit OFFSET :offset";

        Query query = this.entityManager.createNativeQuery(sqlString, Token.class);
        query.setParameter("interval", 1);
        query.setParameter("maxPrice", maxPrice);
        query.setParameter("limit", limit);
        query.setParameter("offset", offset);

        List<Token> tokens = (List<Token>)query.getResultList();

        if (tokens == null || tokens.size() <= 0) return List.of();

        tokens.forEach(token -> token.setOwner(transactionDAO.getTokenOwner(token)));

        return tokens;
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
        Account account = this.entityManager.find(Account.class, accountId);

        if (account == null) return null;

        try {
            List<Token> tokens = account.getTransactionsTo().stream().map(transaction -> transaction.getToken()).filter(token -> {
                boolean accountOwnsToken = transactionDAO.getAccountTokenBalance(token, account) > 0;

                if (accountOwnsToken) token.setOwner(account);

                return accountOwnsToken;
            }).toList();
            
            return tokens.subList(offset, offset + limit <= tokens.size() ? offset + limit : tokens.size());
        } catch (IndexOutOfBoundsException ex) {
            return List.of();
        }
    }
    
    public List<Token> getTokensForSaleBy (String accountId, int limit, int offset) {
        Account account = this.entityManager.find(Account.class, accountId);

        if (account == null) return null;

        try {
            List<Token> tokens = Stream.concat(
                    account.getTransactionsTo().stream().map(transaction -> transaction.getToken()),
                    account.getCreatedNfts().stream()
                ).toList();
            
            tokens = tokens.stream().filter(token -> {
                boolean accountOwnsToken = transactionDAO.getAccountTokenBalance(token, account) > 0;
                
                if (accountOwnsToken) token.setOwner(account);

                return accountOwnsToken && token.getSettings().isForSale();
            }).toList();

            return tokens.subList(offset, offset + limit <= tokens.size() ? offset + limit : tokens.size());
        } catch (IndexOutOfBoundsException ex) {
            return List.of();
        }
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

        tokenslist.forEach(token -> token.setOwner(transactionDAO.getTokenOwner(token)));
    
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

        tokenslist.forEach(token -> token.setOwner(transactionDAO.getTokenOwner(token)));
    
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

        tokenslist.forEach(token -> token.setOwner(transactionDAO.getTokenOwner(token)));
    
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

        tokenslist.forEach(token -> token.setOwner(transactionDAO.getTokenOwner(token)));
    
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

        tokenslist.forEach(token -> token.setOwner(transactionDAO.getTokenOwner(token)));
    
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

        tokenslist.forEach(token -> token.setOwner(transactionDAO.getTokenOwner(token)));
    
        return tokenslist;
    }
}
