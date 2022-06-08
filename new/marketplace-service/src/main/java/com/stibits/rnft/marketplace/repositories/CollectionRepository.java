package com.stibits.rnft.marketplace.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.stibits.rnft.marketplace.domain.Collection;

@Repository
public class CollectionRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Collection insertCollection (String name, String description, String creatorId, String imageUrl) {
        Collection collection = new Collection();

        collection.setName(name);
        collection.setDescription(description);
        collection.setCreatedById(creatorId);
        collection.setImageUrl(imageUrl);

        this.entityManager.persist(collection);

        return collection;
    }

    public Collection selectCollectionById (String id) {
        return this.entityManager.find(Collection.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Collection> selectCollectionsByPopularity (int limit, int offset) {
        String sqlString = "SELECT c.id, c.created_by_id, c.created_date, c.description, c.image_url, c.name, " +
            "COUNT(lk.account_id) as likes_count, COUNT(bid.id) AS bids_count " +
            "FROM collection AS c " +
            "LEFT JOIN token AS t ON t.collection_id = c.id " +
            "LEFT JOIN token_like AS lk ON lk.token_id = t.id AND lk.created_date >= NOW() - INTERVAL '1 MONTH' " +
            "LEFT JOIN bid ON bid.token_id = t.id AND bid.created_date >= NOW() - INTERVAL '1 MONTH' " +
            "GROUP BY c.id, c.created_by_id, c.created_date, c.description, c.image_url, c.name " +
            "ORDER BY bids_count DESC, likes_count DESC, created_date DESC " +
            "LIMIT :limit OFFSET :offset ";

        Query query = this.entityManager.createNativeQuery(sqlString, Collection.class);
        query.setParameter("limit", limit);
        query.setParameter("offset", offset);

        return (List<Collection>)query.getResultList();
    }
}
