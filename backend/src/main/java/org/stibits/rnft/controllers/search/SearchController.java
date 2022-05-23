package org.stibits.rnft.controllers.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.stibits.rnft.converters.CollectionMapConverter;
import org.stibits.rnft.converters.ProfileDetailsConverter;
import org.stibits.rnft.converters.TokenMapConverter;
import org.stibits.rnft.domain.Account;
import org.stibits.rnft.domain.NftCollection;
import org.stibits.rnft.domain.Token;
import org.stibits.rnft.repositories.SearchDAO;

@RestController
@RequestMapping("/api/${api.version}/search")
public class SearchController {
    @Autowired
    private SearchDAO searchDAO;

    @Autowired
    private TokenMapConverter tokenConverter;

    @Autowired
    private CollectionMapConverter collectionConverter;

    @Autowired
    private ProfileDetailsConverter profileConverter;

    private int itemsPerPage = 10;

    @GetMapping("/items")
    public Map<String, Object> getSearchItems (@RequestParam("query") String query, @RequestParam(name = "page", defaultValue = "1", required = false) int page) {
        Map<String, Object> response = new HashMap<>();

        page = page > 0 ? page : 1;

        List<Token> result = searchDAO.searchForToken(query, itemsPerPage, (page - 1) * itemsPerPage);

        response.put("success", true);
        response.put("data", tokenConverter.convertList(result));

        return response;
    }

    @GetMapping("/collections")
    public Map<String, Object> getSearchCollections (@RequestParam("query") String query, @RequestParam(name = "page", defaultValue = "1", required = false) int page) {
        Map<String, Object> response = new HashMap<>();

        page = page > 0 ? page : 1;

        List<NftCollection> result = searchDAO.searchForCollections(query, itemsPerPage, (page - 1) * itemsPerPage);

        response.put("success", true);
        response.put("data", collectionConverter.convert(result));

        return response;
    }

    @GetMapping("/users")
    public Map<String, Object> getSearchUsers (@RequestParam("query") String query, @RequestParam(name = "page", defaultValue = "1", required = false) int page) {
        Map<String, Object> response = new HashMap<>();

        page = page > 0 ? page : 1;

        List<Account> result = searchDAO.searchForAccount(query, itemsPerPage, (page - 1) * itemsPerPage);

        response.put("success", true);
        response.put("data", profileConverter.convertAccountList(result));

        return response;
    }
}
