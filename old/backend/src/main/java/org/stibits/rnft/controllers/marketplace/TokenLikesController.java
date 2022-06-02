package org.stibits.rnft.controllers.marketplace;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.stibits.rnft.domain.Account;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.DataIntegrityError;
import org.stibits.rnft.errors.NotFoundError;
import org.stibits.rnft.repositories.NFTokenDAO;

@RestController
@RequestMapping("/api/${api.version}/marketplace/like")
public class TokenLikesController {
    @Autowired
    public NFTokenDAO nftokenDAO;

    @PostMapping
    public Map<String, Object> handlePostRequest (@RequestBody Map<String, Object> data, @RequestAttribute("account") Account account) throws ApiError {
        Map<String, Object> response = new HashMap<>();

        if (!data.containsKey("id") || !data.get("id").getClass().equals(String.class)) throw new NotFoundError();

        try {
            Boolean created = nftokenDAO.likeItem(account.getId(), (String)data.get("id"));
            response.put("success", created);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityError("Token with this id does not exist or already liked", "id");
        }
        
        return response;
    }

    @DeleteMapping
    public Map<String, Object> handleDeleteRequest (@RequestParam("id") String tokenId, @RequestAttribute("account") Account account) throws ApiError {
        Map<String, Object> response = new HashMap<>();

        boolean deleted = nftokenDAO.unlikeItem(account.getId(), tokenId);
        
        if (!deleted) throw new NotFoundError();
        response.put("success", true);

        return response;
    }
}
