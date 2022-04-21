package org.merchantech.nftproject.controllers.profile;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.merchantech.nftproject.errors.ApiError;
import org.merchantech.nftproject.errors.UnknownError;
import org.merchantech.nftproject.errors.ValidationError;
import org.merchantech.nftproject.model.bo.Account;
import org.merchantech.nftproject.model.bo.Profile;
import org.merchantech.nftproject.model.dao.ProfileDAO;
import org.merchantech.nftproject.validation.ProfileFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/${api.version}/profile")
public class ProfileController {

	@Autowired
	private ProfileFormValidator formValidator;

	@Autowired
	private ProfileDAO profiledao;

	@Autowired
	private MessageSource messageSource;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> handlePostRequest(@RequestBody Map<String, Object> data,
			@RequestAttribute(name = "account", required = true) Account account, HttpServletRequest request)
			throws ApiError {
		Map<String, Object> response = new HashMap<>();
		MapBindingResult errors = new MapBindingResult(data, "profile");

		response.put("success", true);
		formValidator.validate(data, errors);

		if (errors.hasErrors())
			throw new ValidationError(errors, messageSource);

		try {
			profiledao.insertprofile(data, account);
		} catch (Exception ex) {
			System.out.println(ex);
			throw new UnknownError();
		}
		return response;
	}

	@GetMapping
	Map<String, Object> handleGetRequest(@RequestAttribute(name = "account", required = true) Account account)throws ApiError {
		Map<String, Object> response = new HashMap<>();

		response.put("success", true);
		try {
			Profile profile = profiledao.getProfilebyId(account.getId());
			response.put("profile", profile);
		} catch (Exception ex) {
			System.out.println(ex);
			throw new UnknownError();
		}

		return response;
	}
}
