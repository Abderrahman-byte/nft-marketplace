package org.stibits.rnft.controllers.marketplace;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stibits.rnft.converters.TokenMapConverter;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.Token;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.NotFoundError;
import org.stibits.rnft.repositories.NFTokenDAO;

@RestController
@RequestMapping("/api/${api.version}/marketplace/tokens/{id}")
public class GetTokenDetails {
    @Autowired
    NFTokenDAO nftokenDAO;

    @Autowired
    TokenMapConverter tokenMapConverter;

    @GetMapping
    public Map<String, Object> handleGetRequest (@PathVariable(name = "id", required = false) String tokenId, @RequestAttribute(name = "account", required = false) Account account) throws ApiError {
        if (tokenId == null || tokenId.equals("")) throw new NotFoundError();

        Map<String, Object> response = new HashMap<>();
        Token token = nftokenDAO.selectToken(tokenId);

        if (token == null) throw new NotFoundError();

        response.put("success", true);
        response.put("data", tokenMapConverter.convert(token, account));

        return response;
    }
}
