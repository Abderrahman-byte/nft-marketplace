package org.merchantech.nftproject.controllers.auth;

import java.util.HashMap;
import java.util.Map;

import org.merchantech.nftproject.errors.ApiError;
import org.merchantech.nftproject.errors.ValidationError;
import org.merchantech.nftproject.model.bo.Account;
import org.merchantech.nftproject.model.dao.AccountDAO;
import org.merchantech.nftproject.utils.PasswordHasher;
import org.merchantech.nftproject.validation.LoginFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO : implement session management

//http://localhost:8080/api/v1/auth/login
@RestController
@RequestMapping("/api/${api.version}/auth/login")
public class LoginController {
	@Autowired
	private LoginFormValidator formValidator;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private AccountDAO accountDAO;

	private Account temp = new Account();

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> handlePostRequest(@RequestBody Map<String, Object> data) throws ApiError {
		Map<String, Object> response = new HashMap<>();
		MapBindingResult errors = new MapBindingResult(data, "login");

		response.put("success", true);
		formValidator.validate(data, errors);

		if (errors.hasErrors()) throw new ValidationError(errors, messageSource);

		temp = accountDAO.getAccountByUsername(String.valueOf(data.get("username")));
		
		if (temp == null || PasswordHasher.checkHash(temp.getPassword(), String.valueOf(data.get("password"))) == false) {
			response.put("ok", false);
			return response;
		}

		return response;
	}
}
