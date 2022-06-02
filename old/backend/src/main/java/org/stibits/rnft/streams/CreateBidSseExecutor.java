package org.stibits.rnft.streams;

import java.util.Map;


import com.auth0.jwt.interfaces.Claim;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.stibits.rnft.domain.Account;
import org.stibits.rnft.domain.Bid;
import org.stibits.rnft.domain.Notification;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.AuthenticationRequiredError;
import org.stibits.rnft.errors.InvalidData;
import org.stibits.rnft.errors.UnauthorizedError;
import org.stibits.rnft.repositories.BidsDAO;

public class CreateBidSseExecutor extends AbstractStreamExecutor {
    private String refToken;
    private Account account;
    private BidsDAO bidsDAO;

    public CreateBidSseExecutor () {}

    public void setRefToken(String refToken) {
        this.refToken = refToken;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setBidsDAO(BidsDAO bidsDAO) {
        this.bidsDAO = bidsDAO;
    }
    
    protected void execute () throws Exception {
        Map<String, Claim> payload = this.parseRefJwt(this.refToken);

        if (account == null) {
            this.sendAndComplete(this.makeErrorEvent(new AuthenticationRequiredError()));
            return ;
        }

        if (payload == null || !this.checkPayload(payload)) {
            this.sendAndComplete(this.makeErrorEvent(new InvalidData()));
            return ;
        }
        
        String accountId = payload.get("accountId").asString();
        String tokenId = payload.get("tokenId").asString();
        String toId = payload.get("to").asString();
        double price = payload.get("price").asDouble();
        
        if (!accountId.equals(account.getId())) {
            this.sendAndComplete(this.makeErrorEvent(new UnauthorizedError()));
            return ;
        }

        // Receive Qr code from the api
        String qrCode = accountId + ":" + toId + ":" + tokenId + ":" + price;
        qrCode = Base64.encodeBase64String(qrCode.getBytes());

        Thread.sleep(1000);

        this.emitter.send(SseEmitter.event().name("qr").data(qrCode));

        // Wait untill the user accept the bid through qr code scanning
        Thread.sleep(5000);

        try {
            Bid bid = this.bidsDAO.insertBid(account, toId, tokenId, price);
            Notification notification = notificationDAO.insertNotification(bid);
            notificationPublisher.publish(notification);
        } catch (ApiError ex) {
            this.sendAndComplete(this.makeErrorEvent(ex));
            return ;
        }

        this.emitter.send(SseEmitter.event().name("accepted").data(""));
        this.emitter.complete();
    }

    private boolean checkPayload(Map<String, Claim> payload) {
        return payload != null && payload.containsKey("action") && 
            payload.get("action") != null && payload.get("action").asString().equals("offer") &&
            payload.containsKey("tokenId") && payload.get("tokenId") != null &&
            payload.containsKey("to") && payload.get("to") != null &&
            payload.containsKey("accountId") && payload.get("accountId") != null &&
            payload.containsKey("price") && payload.get("price") != null;
    }
}
