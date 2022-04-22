package org.stibits.rnft.model.dao;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
