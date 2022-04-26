package org.stibits.rnft.controllers.auth;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.Session;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.UnverifiedEmailError;
import org.stibits.rnft.errors.ValidationError;
import org.stibits.rnft.errors.WrongCredentialsError;
import org.stibits.rnft.repositories.AccountDAO;
import org.stibits.rnft.repositories.SessionDAO;
import org.stibits.rnft.utils.PasswordHasher;
import org.stibits.rnft.utils.RandomGenerator;
import org.stibits.rnft.validation.LoginFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	@Autowired 
	private SessionDAO sessionDAO;
	
	@Autowired
    private RandomGenerator randomGenerator;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> handlePostRequest(@RequestBody Map<String, Object> data,HttpServletResponse httpResponse) throws ApiError {
		Map<String, Object> response = new HashMap<>();
		MapBindingResult errors = new MapBindingResult(data, "login");
	
		formValidator.validate(data, errors);

		if (errors.hasErrors()) throw new ValidationError(errors, messageSource);

		Account temp = authenticate(String.valueOf(data.get("username")), String.valueOf(data.get("password")));

		if (temp == null) throw new WrongCredentialsError();

		if (!temp.isVerified()) throw new UnverifiedEmailError();

		response.put("success", true);
		saveSession(httpResponse, temp);

		return response;
	}
	
	private void saveSession(HttpServletResponse httpResponse, Account temp2) {
		Map<String, Object> payload = new HashMap<>();
		Session session = new Session();
		Calendar c = Calendar.getInstance();

		c.add(Calendar.MONTH, 1);
		payload.put("currentUser", temp2.getId());

		session.setPayload(payload);
		session.setSid(randomGenerator.generateRandomStr(15));
		session.setExpires(c);
		sessionDAO.insertSession(session);

		if (session != null) {
			Cookie cookie = new Cookie("sessionId", session.getSid());
			cookie.setMaxAge((int) session.getMaxAge());
			cookie.setPath("/");
			httpResponse.addCookie(cookie);
		}
	}

	public Account authenticate(String username, String password) {
        Account account = accountDAO.getAccountByUsername(username);

        return account == null || !PasswordHasher.checkHash(account.getPassword(), password) ? null : account;
    }
}
