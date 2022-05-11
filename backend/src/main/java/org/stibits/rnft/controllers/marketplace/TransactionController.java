package org.stibits.rnft.controllers.marketplace;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.Token;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.AuthenticationRequiredError;
import org.stibits.rnft.errors.DataIntegrityError;
import org.stibits.rnft.errors.TokenNotFound;
import org.stibits.rnft.errors.ValidationError;
import org.stibits.rnft.executors.TransactionSseExecutor;
import org.stibits.rnft.repositories.NFTokenDAO;
import org.stibits.rnft.repositories.TransactionDAO;
import org.stibits.rnft.validation.CreateTransValidator;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@RestController
@RequestMapping("/api/${api.version}/marketplace/buy")
public class TransactionController {
	@Autowired
	private TransactionDAO transactionDAO;
	@Autowired
	private NFTokenDAO tokenDao;
	@Autowired
	private CreateTransValidator validator;
	@Autowired
	private MessageSource messageSource;

	@Value("${jwt.secret}")
	private String jwtSecret;

	@PostMapping
	public Map<String, Object> handlePostRequest(@RequestBody Map<String, Object> data, @RequestAttribute("account") Account account) throws ApiError {
		Map<String, Object> response = new HashMap<>();
		MapBindingResult errors = new MapBindingResult(data, "transaction");

		if (account == null) throw new AuthenticationRequiredError();

		validator.validate(data, errors);

		if (errors.hasErrors()) throw new ValidationError(errors, messageSource);

		String TokenId = (String) data.get("id");
		Token token = tokenDao.selectTokenById(TokenId);

		if (token == null) throw new TokenNotFound();
		
		if (!token.getSettings().isInstantSale()) throw new DataIntegrityError("Token is not for sell", "tokenId");

		data.put("toId", account.getId());
		data.put("fromId", token.getOwner().getId());
		data.put("action", "transaction");
		data.put("price", token.getSettings().getPrice());

		Algorithm algo = Algorithm.HMAC256(jwtSecret);
		String refToken = JWT.create()
				.withExpiresAt(Date.from(Instant.now().plusSeconds(60 * 5)))
				.withPayload(data)
				.sign(algo);
		
		response.put("ref", refToken);
		response.put("success", true);

		return response;
	}

	@GetMapping
	public SseEmitter createTransaction(@RequestAttribute("account") Account account, @RequestParam("ref") String ref) {
		SseEmitter emitter = new SseEmitter();
		ExecutorService sseExecutor = Executors.newSingleThreadExecutor();
		TransactionSseExecutor executor = new TransactionSseExecutor();

		executor.setAccount(account);
		executor.setTransDAO(transactionDAO);
		executor.setEmitter(emitter);
		executor.setJwtSecret(jwtSecret);
		executor.setRefToken(ref);

		sseExecutor.execute(executor);

		return emitter;
	}
}
