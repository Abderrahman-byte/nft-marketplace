package org.merchantech.nftproject.controllers.auth;

import java.util.HashMap;
import java.util.Map;

import org.merchantech.nftproject.errors.ApiError;
import org.merchantech.nftproject.errors.DataIntegrityError;
import org.merchantech.nftproject.errors.UnknownError;
import org.merchantech.nftproject.errors.ValidationError;
import org.merchantech.nftproject.model.dao.AccountDAO;
import org.merchantech.nftproject.validation.RegisterFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    Map<String, Object> handlePostRequest (@RequestBody Map<String, Object> data) throws ApiError {
        Map<String, Object> response = new HashMap<>();
        MapBindingResult errors = new MapBindingResult(data, "register");

        response.put("success", true);
        formValidator.validate(data, errors);

        if (errors.hasErrors()) throw new ValidationError(errors, messageSource);

        try {
            accountDAO.insertAccount(data);
        } catch (DataIntegrityViolationException ex) {
            throw this.translateDataIntegrityError(ex);
        } catch (Exception ex) {
            throw new UnknownError();
        }

        return response;
    }

    private ApiError translateDataIntegrityError (DataIntegrityViolationException ex) {
        String message = ex.getMessage();

        if (message.contains("email")) {
            return new DataIntegrityError("An account with same email already exists.", "email");
        } 
        
        if (message.contains("username")) {
            return new DataIntegrityError("An account with same email already exists.", "username");
        }

        return new UnknownError();
    }
}
