package org.stibits.rnft.websocket;

import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.notifications.NotificationsConsumer;
import org.stibits.rnft.notifications.converters.NotificationJsonConverter;
import org.stibits.rnft.repositories.NotificationDAO;

public class NotificationsHandler extends TextWebSocketHandler {
    @Autowired
    private Connection connection;

    @Autowired
    private NotificationJsonConverter notificationConverter;

    @Autowired
    private NotificationDAO notificationDAO;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${rmq.notifications.exchange:notifications}")
    private String notificationExchange;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        NotificationsConsumer consumer = new NotificationsConsumer(connection, session);
        consumer.setExchange(notificationExchange);
        consumer.setNotificationConverter(notificationConverter);
        consumer.setNotificationDAO(notificationDAO);
        consumer.setObjectMapper(objectMapper);

        consumer.start();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // TODO : close broker channel mercufuly
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            List<Object> eventMessage = objectMapper.readValue(message.getPayload(), List.class);
            String eventName = null;
            Object eventPayload = null;

            if (eventMessage.size() <= 0) return ;

            if (!eventMessage.get(0).getClass().equals(String.class)) return ;

            eventName = (String)eventMessage.get(0);

            if (eventMessage.size() >= 2) eventPayload = eventMessage.get(1);

            try {
                this.handleEvent(session, eventName, eventPayload);
            } catch (Exception ex) {
                System.out.println("[ERROR:NotificationsHandler.handleTextMessage] " + ex.getMessage());
            }
        } catch (JsonProcessingException ex) {}
    }

    private void handleEvent (WebSocketSession session, String event, Object payload) throws Exception {
        Account account = (Account)session.getAttributes().get("account");

        if (event.equals("vued") && payload.getClass().equals(Long.class)) {
            long timestamp = Long.valueOf((Long)payload);
            Calendar lastSeen = Calendar.getInstance();
            lastSeen.setTimeInMillis(timestamp);
            this.notificationDAO.updateVuedNotifications(account.getId(), lastSeen);
        }
    }
}
