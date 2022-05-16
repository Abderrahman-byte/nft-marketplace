package org.stibits.rnft.streams;

import java.io.IOException;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.notifications.NotificationsPublisher;
import org.stibits.rnft.repositories.NotificationDAO;

public abstract class AbstractStreamExecutor implements Runnable {
    private ObjectMapper objectMapper = new ObjectMapper();
    protected SseEmitter emitter;
    protected String jwtSecret;
    protected NotificationDAO notificationDAO;
    protected NotificationsPublisher notificationPublisher;

    public void setNotificationDAO(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    public void setNotificationPublisher(NotificationsPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }

    public void setEmitter(SseEmitter emitter) {
        this.emitter = emitter;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @Override
    public void run() {
        try {
            this.execute();
        } catch (Exception ex) {
            System.err.println("[ERROR] " + ex.getMessage());
            this.emitter.complete();
        }
    }

    protected abstract void execute () throws Exception;

    protected SseEventBuilder makeErrorEvent (ApiError error) throws JsonProcessingException {
        return SseEmitter.event()
            .name("error")
            .data(objectMapper.writeValueAsString(error));
    }

    protected void sendAndComplete (SseEventBuilder event) throws IOException {
        this.emitter.send(event);
        this.emitter.complete();
    }

    protected Map<String, Claim> parseRefJwt (String ref) {
        Algorithm algorithm = Algorithm.HMAC256(this.jwtSecret);

        try {
            return JWT.require(algorithm).build().verify(ref).getClaims();
        } catch (JWTVerificationException ex) {
            return null;
        }
    }
}
