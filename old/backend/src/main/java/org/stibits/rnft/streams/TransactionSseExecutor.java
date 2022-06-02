package org.stibits.rnft.streams;

import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.stibits.rnft.domain.Account;
import org.stibits.rnft.domain.Notification;
import org.stibits.rnft.domain.Transaction;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.AuthenticationRequiredError;
import org.stibits.rnft.errors.InvalidData;
import org.stibits.rnft.errors.UnauthorizedError;
import org.stibits.rnft.repositories.NFTokenDAO;
import org.stibits.rnft.repositories.TransactionDAO;

import com.auth0.jwt.interfaces.Claim;

public class TransactionSseExecutor extends AbstractStreamExecutor {
	private NFTokenDAO tokenDAO;
	private String refToken;
	private Account account;
	private TransactionDAO transDAO;

	public void setRefToken(String refToken) {
		this.refToken = refToken;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void setTransDAO(TransactionDAO transDAO) {
		this.transDAO = transDAO;
	}

	public void setTokenDAO(NFTokenDAO tokenDAO) {
		this.tokenDAO = tokenDAO;
	}

	protected void execute() throws Exception {
		Map<String, Claim> payload = this.parseRefJwt(this.refToken);

		if (account == null) {
			this.sendAndComplete(this.makeErrorEvent(new AuthenticationRequiredError()));
			return;
		}

		if (!this.checkPayload(payload)) {
			this.sendAndComplete(this.makeErrorEvent(new InvalidData()));
			return;
		}

		String accountId = payload.get("toId").asString();
		String tokenId = payload.get("id").asString();
		String fromId = payload.get("fromId").asString();
		double price = payload.get("price").asDouble();

		if (!accountId.equals(account.getId())) {
			this.sendAndComplete(this.makeErrorEvent(new UnauthorizedError()));
			return;
		}

		String qrCode = accountId + ":" + fromId + ":" + tokenId + ":" + price;
		qrCode = Base64.encodeBase64String(qrCode.getBytes());

		Thread.sleep(1000);

		this.emitter.send(SseEmitter.event().name("qr").data(qrCode));

		Thread.sleep(5000);

		try {
			Transaction transaction = this.transDAO.insertTransaction(tokenId, fromId, accountId, price);
			this.tokenDAO.updateTokenSettings(tokenId, true, false, price);
			this.emitter.send(SseEmitter.event().name("accepted").data(""));
			this.emitter.complete();

			Notification notification = this.notificationDAO.insertNotification(transaction);
			this.notificationPublisher.publish(notification);
		} catch (ApiError ex) {
			this.sendAndComplete(this.makeErrorEvent(ex));
			return;
		}
	}

	private boolean checkPayload(Map<String, Claim> payload) {
		return payload != null && payload.containsKey("action")
		&& payload.get("action") !=  null && payload.get("action").asString().equals("transaction")
		&& payload.containsKey("toId") && payload.get("toId") != null
		&& payload.containsKey("id") && payload.get("id") != null 
		&& payload.containsKey("fromId") && payload.get("fromId") != null 
		&& payload.containsKey("price") && payload.get("price") != null;
	}
}
