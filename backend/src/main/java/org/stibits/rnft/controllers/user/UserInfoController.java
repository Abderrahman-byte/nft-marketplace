package org.stibits.rnft.controllers.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.stibits.rnft.converters.ProfileDetailsConverter;
import org.stibits.rnft.converters.TokenMapConverter;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.NFToken;
import org.stibits.rnft.entities.Profile;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.NotFoundError;
import org.stibits.rnft.repositories.NFTokenDAO;
import org.stibits.rnft.repositories.ProfileDAO;

@RestController
@RequestMapping("/api/${api.version}/user/{id}")
public class UserInfoController {
    @Autowired
    public ProfileDAO profileDAO;

    @Autowired
    public NFTokenDAO nfTokenDAO;

    @Autowired
    public ProfileDetailsConverter profileConverter;

    @Autowired
    public TokenMapConverter tokenConverter;

    @GetMapping()
    public Map<String, Object> getUserDetails (@PathVariable("id") String accountId) throws ApiError {
        Map<String, Object> response = new HashMap<>();

        Profile profile = profileDAO.getProfilebyId(accountId);

        if (profile == null) throw new NotFoundError();

        response.put("success", true);
        response.put("data", profileConverter.convert(profile));

        return response;
    }

    // TODO : Role param should represented by enum
    // TODO : must check if account exists

    @GetMapping("/tokens")
    public Map<String, Object> getUserItems (
        @PathVariable("id") String accountId,
        @RequestParam(name="limit", required = false, defaultValue = "50") int limit,
        @RequestParam(name="offset", required = false, defaultValue = "0") int offset,
        @RequestParam(name = "role", required = false, defaultValue = "CREATOR") String role,
        @RequestAttribute(name = "account", required = false) Account account
    ) {
        Map<String, Object> response = new HashMap<>();
        List<NFToken> tokens = new ArrayList<>();

        if (!List.of("CREATOR", "SALE", "OWNER", "FAVORITE").contains(role)) role = "CREATOR";

        if (role.equals("SALE")) {
            tokens = nfTokenDAO.getTokensForSaleBy(accountId, limit, offset);
        } else if (role.equals("OWNER")) {
            tokens = nfTokenDAO.getTokensOwnedBy(accountId, limit, offset);
        } else if (role.equals("FAVORITE")) {
            tokens = nfTokenDAO.getUserFavoriteTokens(accountId, limit, offset);
        } else {
            tokens = nfTokenDAO.getTokensCreatedBy(accountId, limit, offset);
        }

        response.put("success", true);
        response.put("data", tokenConverter.convertList(tokens, account));

        return response;
    }
}
