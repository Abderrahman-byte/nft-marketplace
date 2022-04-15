package org.merchantech.nftproject.controllers.auth;

import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.merchantech.nftproject.errors.ApiError;
import org.merchantech.nftproject.errors.DataIntegrityError;
import org.merchantech.nftproject.errors.UnknownError;
import org.merchantech.nftproject.errors.ValidationError;
import org.merchantech.nftproject.helpers.MailService;
import org.merchantech.nftproject.model.bo.Account;
import org.merchantech.nftproject.model.dao.AccountDAO;
import org.merchantech.nftproject.validation.RegisterFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("emailTemplateEngine")
    private TemplateEngine templateEngine;
	
    @Autowired
    private RegisterFormValidator formValidator;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private MailService mailService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> handlePostRequest (@RequestBody Map<String, Object> data) throws ApiError {
        Map<String, Object> response = new HashMap<>();
        MapBindingResult errors = new MapBindingResult(data, "register");

        response.put("success", true);
        formValidator.validate(data, errors);

        if (errors.hasErrors()) throw new ValidationError(errors, messageSource);

        try {
            Account account = accountDAO.insertAccount(data);
            this.sendVerificationEmail(account);
        } catch (DataIntegrityViolationException ex) {
            throw this.translateDataIntegrityError(ex);
        } catch (Exception ex) {
            throw new UnknownError();
        }

        return response;
    }

    private void sendVerificationEmail (Account account) {
        Context ctx = new Context();
        ctx.setVariable("account", account);

        String content = templateEngine.process("email-verification", ctx);

        mailService.sendMail(account.getEmail(), "Verify Email", content, "text/html; charset=utf-8");
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
