package org.stibits.rnft.model.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.stibits.rnft.model.bo.Account;
import org.stibits.rnft.model.bo.NftCollection;
import org.stibits.rnft.utils.RandomGenerator;

@Repository
public class NftCollectionDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RandomGenerator randomGenerator;

    public NftCollection getCollectionById (String id) {
        return this.entityManager.find(NftCollection.class, id);
    }

    @Transactional
    public NftCollection insertCollection(Account creator, Map<String, Object> data, String imageUrl) {
        return this.insertCollection(creator, (String)data.get("name"), (String)data.get("description"), imageUrl);
    }

    @Transactional
    public NftCollection insertCollection (Account creator, String name, String description, String imageUrl) {
        NftCollection collection = new NftCollection();

        collection.setId(randomGenerator.generateRandomStr(25));
        collection.setCreatedBy(creator);
        collection.setImageUrl(imageUrl);
        collection.setDescription(description);
        collection.setName(name);

        return entityManager.merge(collection);
    }

    public NftCollection selectCollectionById (String id) {
        return entityManager.find(NftCollection.class, id);
    }

    // TODO : maybe add likes date filter (ex: Trend collection of the last week)
    @SuppressWarnings("unchecked")
    public List<NftCollection> selectPopulareCollections (int limit, int offset) {
        String sqlString = "SELECT c.id, c.name, c.created_by,c.description,c.image_url,c.created_date, count(lk.account_id) "+ 
        "FROM nft_collection AS c " + 
        "LEFT JOIN nft_token AS nft ON nft.collection_id is not NULL AND c.id = nft.collection_id "+ 
        "LEFT JOIN nft_token_likes AS lk ON nft.id = lk.token_id "+
        "GROUP BY c.id,c.name,c.created_by,c.description,c.image_url,c.created_date limit :limit";

        Query query = entityManager.createNativeQuery(sqlString, NftCollection.class);

        query.setParameter("limit", limit);

        return (List<NftCollection>)query.getResultList();
    }
}
