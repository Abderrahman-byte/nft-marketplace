package org.stibits.rnft.controllers.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stibits.rnft.converters.SimpleCollectionMapConverter;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.NftCollection;
import org.stibits.rnft.repositories.NftCollectionDAO;

@RestController
@RequestMapping("/api/${api.version}/user/collections")
public class GetUserCollectionsController {
    @Autowired
    private NftCollectionDAO collectionDAO;

    @Autowired
    private SimpleCollectionMapConverter collectionConverter;

    @GetMapping
    public Map<String, Object> handleGetRequest (@RequestAttribute("account") Account account) {
        Map<String, Object> response = new HashMap<>();
        List<NftCollection> collections = collectionDAO.selectCollectionsByAccountId(account.getId());

        response.put("data", collectionConverter.convertList(collections));
        response.put("success", true);

        return response;
    }
}
