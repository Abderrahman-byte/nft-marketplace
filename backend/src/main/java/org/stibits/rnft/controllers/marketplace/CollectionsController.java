package org.stibits.rnft.controllers.marketplace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.stibits.rnft.converters.CollectionMapConverter;
import org.stibits.rnft.converters.SimpleCollectionMapConverter;
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

    @Autowired
    private SimpleCollectionMapConverter simpleCollectionMapConverter;

    @GetMapping("/{id}")
    public Map<String, Object> getCollectionDetails (@PathVariable("id") String collectionId) throws ApiError {
        Map<String, Object> response = new HashMap<>();

        NftCollection collection = collectionDAO.getCollectionById(collectionId);

        if (collection == null) throw new CollectionNotFound();

        response.put("data", collectionConverter.convert(collection));
        response.put("success", true);

        return response;
    }

    // This controller function respond with a list of most liked collections
    //  * We may need to use multiple filters

    @GetMapping()
    public Map<String, Object> getPopulareCollections (
        @RequestParam(name="limit", required = false, defaultValue = "20") int limit,
        @RequestParam(name="offset", required = false, defaultValue = "0") int offset
    ) {
        Map<String, Object> response = new HashMap<>();

        if (limit <= 0) limit = 20;
        if (offset < 0) offset = 0;

        List<NftCollection> collections = collectionDAO.selectPopulareCollections(limit, offset);

        response.put("success", true);
        response.put("data", simpleCollectionMapConverter.convertList(collections));

        return response;
    }
}
