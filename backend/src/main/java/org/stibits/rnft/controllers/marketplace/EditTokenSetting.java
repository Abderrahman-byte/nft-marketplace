package org.stibits.rnft.controllers.marketplace;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stibits.rnft.domain.Account;
import org.stibits.rnft.domain.Token;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.InvalidData;
import org.stibits.rnft.errors.NotFoundError;
import org.stibits.rnft.errors.UnknownError;
import org.stibits.rnft.errors.ValidationError;
import org.stibits.rnft.repositories.NFTokenDAO;
import org.stibits.rnft.repositories.TransactionDAO;
import org.stibits.rnft.validation.EditTokenSettingsValidation;

@RestController
@RequestMapping("/api/${api.version}/marketplace/tokens/{id}")
public class EditTokenSetting {
    @Autowired
    private NFTokenDAO tokenDAO;

    @Autowired
    private TransactionDAO transactionDAO;

    @Autowired
    private EditTokenSettingsValidation tokenSettingsValidation;

    @Autowired
    private MessageSource messageSource;

    @PutMapping
    public Map<String, Object> handlePutRequest (@RequestAttribute(required = false, name = "account") Account account, @PathVariable(name = "id") String tokenId, @RequestBody Map<String, Object> data) throws ApiError {
        Token token = tokenDAO.selectTokenById(tokenId);

        if (token == null || transactionDAO.getAccountTokenBalance(token, account) <= 0) throw new NotFoundError();

        MapBindingResult errors = new MapBindingResult(data, "nft");

        if (data.size() <= 0) throw new InvalidData();

        tokenSettingsValidation.validate(data, errors);

        if (errors.hasErrors()) throw new ValidationError(errors, messageSource);

        try {
            Map<String, Object> response = new HashMap<>();

            tokenDAO.updateTokenSettings(token, data);
            response.put("success", true);

            return response;
        } catch (Exception ex) {
            System.out.println("[EXCEPTION] " + ex.getMessage());
            throw new UnknownError();
        }
    }
}
