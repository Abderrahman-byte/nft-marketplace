package org.merchantech.nftproject.controllers.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.merchantech.nftproject.model.dao.AccountDAO;
import org.merchantech.nftproject.validation.RegisterFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO : Send email verification

@RestController
@RequestMapping("/api/${api.version}/auth/register")
public class RegisterController {
    @Autowired
    private RegisterFormValidator formValidator;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AccountDAO accountDAO;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> handlePostRequest (@RequestBody Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        MapBindingResult errors = new MapBindingResult(data, "register");

        response.put("ok", true);
        formValidator.validate(data, errors);

        if (errors.hasErrors()) {
            response.put("errors", this.getErrorList(errors));
            response.put("ok", false);
            return response;
        }

        try {
            accountDAO.insertAccount(data);
        } catch (DataIntegrityViolationException ex) {
            response.put("ok", false);
            response.put("errors", this.getTransactionsErrors(ex));
        } catch (Exception ex) {
            response.put("ok", false);
            response.put("errors", List.of("unkown_errors"));
        }

        return response;
    }

    // TODO : must be replaced with Probelem details
    private List<String> getErrorList (Errors errors) {
        List<String> errorsString = new ArrayList<>();
        List<FieldError> fieldErrors = errors.getFieldErrors();

        for (FieldError error: fieldErrors) {
            errorsString.add(messageSource.getMessage(error, Locale.US));
        }

        return errorsString;
    }

    private List<String> getTransactionsErrors (DataIntegrityViolationException ex) {
        String message = ex.getMessage();

        if (message.contains("email")) {
            return List.of("An account with same email already exists.");
        } 
        
        if (message.contains("username")) {
            return List.of("An account with same username already exists.");
        }

        return List.of("unkown_error");
    }
}
