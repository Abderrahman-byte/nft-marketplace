package org.stibits.rnft.controllers.marketplace;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stibits.rnft.converters.CollectionMapConverter;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.CollectionNotFound;
import org.stibits.rnft.model.bo.NftCollection;
import org.stibits.rnft.model.dao.NftCollectionDAO;

@RestController
@RequestMapping("/api/${api.version}/marketplace/collections")
public class CollectionsController {
    @Autowired
    private NftCollectionDAO collectionDAO;

    @Autowired
    private CollectionMapConverter collectionConverter;

    @GetMapping("/{id}")
    public Map<String, Object> handleGetRequest (@PathVariable("id") String collectionId) throws ApiError {
        Map<String, Object> response = new HashMap<>();

        NftCollection collection = collectionDAO.getCollectionById(collectionId);

        if (collection == null) throw new CollectionNotFound();

        response.put("data", collectionConverter.convert(collection));
        response.put("success", true);

        return response;
    }

    
}
