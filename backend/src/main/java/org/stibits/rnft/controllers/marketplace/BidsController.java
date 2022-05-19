package org.stibits.rnft.controllers.marketplace;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.Bid;
import org.stibits.rnft.entities.Notification;
import org.stibits.rnft.entities.OfferResponse;
import org.stibits.rnft.entities.Token;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.AuthenticationRequiredError;
import org.stibits.rnft.errors.DataIntegrityError;
import org.stibits.rnft.errors.InvalidData;
import org.stibits.rnft.errors.NotFoundError;
import org.stibits.rnft.errors.TokenNotFound;
import org.stibits.rnft.errors.UnauthorizedError;
import org.stibits.rnft.errors.ValidationError;
import org.stibits.rnft.notifications.NotificationsPublisher;
import org.stibits.rnft.repositories.BidsDAO;
import org.stibits.rnft.repositories.NFTokenDAO;
import org.stibits.rnft.repositories.NotificationDAO;
import org.stibits.rnft.repositories.TransactionDAO;
import org.stibits.rnft.streams.CreateBidSseExecutor;
import org.stibits.rnft.validation.CreateBidValidator;

@RestController
@RequestMapping("/api/${api.version}/marketplace/bids")
public class BidsController {
    @Autowired
    private TransactionDAO transactionDAO;
	
    @Autowired
    private CreateBidValidator validator;

    @Autowired
    private NFTokenDAO tokenDAO;

    @Autowired
    private BidsDAO bidsDAO;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private NotificationDAO notificationDAO;

    @Autowired
    private NotificationsPublisher notificationPublisher;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @PostMapping
    public Map<String, Object> initiateBidStream (@RequestAttribute(name = "account", required = false) Account account, @RequestBody Map<String, Object> data) throws ApiError {
        MapBindingResult errors = new MapBindingResult(data, "bid");
        Map<String, Object> response = new HashMap<>();

        if (account == null) throw new AuthenticationRequiredError();

        validator.validate(data, errors);

        if (errors.hasErrors()) throw new ValidationError(errors, messageSource);

        String tokenId = (String) data.get("tokenId");
        String to = (String) data.get("to");
        Token token = this.tokenDAO.selectTokenById(tokenId);

        if (token == null) throw new TokenNotFound();

        if (!token.getSettings().isForSale()) throw new DataIntegrityError("Token is not for sell", "tokenId");

        if (transactionDAO.getAccountTokenBalance(token, to) <= 0) throw new DataIntegrityError("Account does not own this token", "to");

        if (account.getId().equals(to)) throw new UnauthorizedError("You cannot bid on your token");

        data.put("accountId", account.getId());
        data.put("action", "offer");
        
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
    public SseEmitter createBid(@RequestAttribute(name = "account", required = false) Account account, @RequestParam("ref") String ref) throws ApiError {
        SseEmitter emitter = new SseEmitter();
        ExecutorService sseExecutor = Executors.newSingleThreadExecutor();
        CreateBidSseExecutor executor = new CreateBidSseExecutor();

        executor.setAccount(account);
        executor.setBidsDAO(bidsDAO);
        executor.setEmitter(emitter);
        executor.setJwtSecret(jwtSecret);
        executor.setRefToken(ref);
        executor.setNotificationDAO(notificationDAO);
        executor.setNotificationPublisher(notificationPublisher);

        sseExecutor.execute(executor);

        return emitter;
    }

    @PostMapping("/{id}")
    public Map<String, Object> respondToOffer (@PathVariable(name = "id") String bidId, @RequestAttribute(name = "account", required = false) Account account, @RequestBody Map<String, Object> data) throws ApiError {
        Map<String, Object> responseData = new HashMap<>();

        if (account == null) throw new AuthenticationRequiredError();

        if (!data.containsKey("action") || !data.get("action").getClass().equals(String.class)) throw new InvalidData();

        String action = (String)data.get("action");

        if (!List.of("accept", "reject").contains(action)) throw new InvalidData();

        Bid bid = bidsDAO.selectBidById(bidId);
        
        if (bid == null) throw new NotFoundError();

        Account owner = this.transactionDAO.getTokenOwner(bid.getToken());

        if (!owner.getId().equals(account.getId()) || 
            !bid.getTo().getId().equals(account.getId()) || 
            !bid.getResponse().equals(OfferResponse.PENDING)) throw new UnauthorizedError();

        OfferResponse response = action.equals("reject") ? OfferResponse.REJECTED : OfferResponse.ACCEPTED;

        if (action.equals("accept")) {
            this.transactionDAO.insertTransaction(bid.getToken(), account, bid.getFrom(), bid.getPrice());
            this.bidsDAO.implicitlyRejectOtherBids(bid.getToken().getId(), account.getId(), bid.getId());
            this.tokenDAO.updateTokenSettings(bid.getToken(), bid.getPrice());
        }

        bid = this.bidsDAO.updateResponse(bid, response);
        responseData.put("success", bid.getResponse().equals(response));
        
        Notification notification = notificationDAO.insertNotification(bid, action.equals("accept"));
        notificationPublisher.publish(notification);

        return responseData;
    }
}
