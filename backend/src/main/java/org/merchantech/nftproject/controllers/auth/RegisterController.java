package org.merchantech.nftproject.controllers.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.merchantech.nftproject.validation.RegisterFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> handlePostRequest (@RequestBody Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        MapBindingResult errors = new MapBindingResult(data, "register");

        // Check received data
        formValidator.validate(data, errors);

        if (errors.hasErrors()) {
            response.put("errors", this.getErrorList(errors));
            response.put("ok", false);
            return response;
        }

        // Create user account
        // send email verification

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
}
