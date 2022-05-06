package org.stibits.rnft.repositories;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.NftCollection;
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
        String sqlString = "select c.id, c.name, c.created_by,c.description,c.image_url,c.created_date "+ 
        "FROM nft_collection AS c " + 
        "LEFT JOIN token AS nft ON nft.collection_id is not NULL AND c.id = nft.collection_id "+ 
        "LEFT JOIN token_likes AS lk ON nft.id = lk.token_id "+
        "GROUP BY c.id,c.name,c.created_by,c.description,c.image_url,c.created_date "+
        "ORDER BY count(lk.account_id) DESC limit :limit";

        Query query = entityManager.createNativeQuery(sqlString, NftCollection.class);

        query.setParameter("limit", limit);

        return (List<NftCollection>)query.getResultList();
    }

    public List<NftCollection> selectCollectionsByAccountId (String accountId) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<NftCollection> cq = cb.createQuery(NftCollection.class);
        Root<NftCollection> root = cq.from(NftCollection.class);

        cq.select(root)
        .where(
            cb.equal(root.get("createdBy").get("id"), accountId)
        ).orderBy(
            cb.desc(root.get("createdDate"))
        );

        return (List<NftCollection>)this.entityManager.createQuery(cq).getResultList();
    }
}
