package org.stibits.rnft.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
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
        // TODO : close broker connection mercufuly
    }
}
