package com.stibits.rnft.marketplace.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
}
