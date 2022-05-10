package org.stibits.rnft.controllers.marketplace;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

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
import org.stibits.rnft.executors.CreateBidSseExecutor;
import org.stibits.rnft.repositories.BidsDAO;
import org.stibits.rnft.repositories.NFTokenDAO;
import org.stibits.rnft.validation.CreateBidValidator;

@RestController
@RequestMapping("/api/${api.version}/marketplace/bids")
public class BidsController {
    @Autowired
    private CreateBidValidator validator;

    @Autowired
    private NFTokenDAO tokenDAO;

    @Autowired
    private BidsDAO bidsDAO;

    @Autowired
    private MessageSource messageSource;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @PostMapping
    public Map<String, Object> initiateBidStream (@RequestAttribute(name = "account", required = false) Account account, @RequestBody Map<String, Object> data) throws ApiError {
        MapBindingResult errors = new MapBindingResult(data, "bid");
        Map<String, Object> response = new HashMap<>();

        if (account == null) throw new AuthenticationRequiredError();

        // Validate data
        validator.validate(data, errors);

        if (errors.hasErrors()) throw new ValidationError(errors, messageSource);

        String tokenId = (String) data.get("tokenId");
        String to = (String) data.get("to");
        Token token = this.tokenDAO.selectTokenById(tokenId);

        // Check if token exists
        if (token == null) throw new TokenNotFound();

        // Check if token is for sale
        if (!token.getSettings().isForSale()) throw new DataIntegrityError("Token is not for sell", "tokenId");

        // Check token ownership
        if (!token.getCreator().getId().equals(to)) throw new DataIntegrityError("Account does not own this token", "to");

        data.put("accountId", account.getId());
        
        // send bid Reference token
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        String refToken = JWT.create()
            .withExpiresAt(Date.from(Instant.now().plusSeconds(60 * 5)))
            .withPayload(data)
            .sign(algorithm);

        response.put("ref", refToken);
        response.put("success", true);

        return response;
    }

    @GetMapping
    public SseEmitter createBid(
            @RequestAttribute("account") Account account,
            @RequestParam("ref") String ref) throws ApiError {

        SseEmitter emitter = new SseEmitter();
        ExecutorService sseExecutor = Executors.newSingleThreadExecutor();
        CreateBidSseExecutor executor = new CreateBidSseExecutor();

        executor.setAccount(account);
        executor.setBidsDAO(bidsDAO);
        executor.setEmitter(emitter);
        executor.setJwtSecret(jwtSecret);
        executor.setRefToken(ref);

        sseExecutor.execute(executor);

        return emitter;
    }
}
