package org.stibits.rnft.controllers.auth;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.DataIntegrityError;
import org.stibits.rnft.errors.UnknownError;
import org.stibits.rnft.errors.ValidationError;
import org.stibits.rnft.helpers.MailService;
import org.stibits.rnft.repositories.AccountDAO;
import org.stibits.rnft.repositories.ProfileDAO;
import org.stibits.rnft.validation.RegisterFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/${api.version}/auth/register")
public class RegisterController {
    @Autowired
    private SpringTemplateEngine templateEngine;
	
    @Autowired
    private RegisterFormValidator formValidator;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
     private MailService mailService;
    @Autowired
    private ProfileDAO profiledao;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> handlePostRequest (@RequestBody Map<String, Object> data, HttpServletRequest request) throws ApiError {
        Map<String, Object> response = new HashMap<>();
        MapBindingResult errors = new MapBindingResult(data, "register");

        response.put("success", true);
        formValidator.validate(data, errors);

        if (errors.hasErrors()) throw new ValidationError(errors, messageSource);

        try {
            Account account = accountDAO.insertAccount(data);
            profiledao.insertProfile(account, account.getUsername(), null, null, null);
            this.sendVerificationEmail(request, account);
        } catch (DataIntegrityViolationException ex) {
            throw this.translateDataIntegrityError(ex);
        } catch (Exception ex) {
            throw new UnknownError();
        }

        return response;
    }

    private void sendVerificationEmail (HttpServletRequest request, Account account) {
        Context ctx = new Context();
        ctx.setVariable("account", account);
        ctx.setVariable("verificationUrl", this.generateVerificationUrl(account, request));

        String content = templateEngine.process("email-verification", ctx);

        mailService.sendMail(account.getEmail(), "Verify Email", content, "text/html; charset=utf-8");
    }

    private String generateVerificationUrl (Account account, HttpServletRequest request) {
        return ServletUriComponentsBuilder
            .fromRequest(request)
            .replacePath("/verify-email")
            .queryParam("tq", this.generateToken(account))
            .build()
            .toUriString();
    }

    private String generateToken(Account account) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            Map<String, Object> payloadClaims = new HashMap<>();
            payloadClaims.put("accountId", account.getId());
            payloadClaims.put("email", account.getEmail());
            payloadClaims.put("action", "verifyEmail");

            return JWT.create().withPayload(payloadClaims).sign(algorithm);
        } catch (JWTCreationException ex) {
            return "";
        }
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
