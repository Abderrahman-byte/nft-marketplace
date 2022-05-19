package org.stibits.rnft.notifications;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.Notification;
import org.stibits.rnft.notifications.converters.NotificationJsonConverter;
import org.stibits.rnft.repositories.NotificationDAO;

public class NotificationsConsumer implements Consumer {
    private Connection rmqConnection;
    private WebSocketSession session;
    private Channel rmqChannel;
    private String exchange;
    private String queue;
    private Map<String, Object> sessionAttributes;
    private Account account;
    private NotificationDAO notificationDAO;
    private NotificationJsonConverter notificationConverter;
    private ObjectMapper objectMapper;

    public NotificationsConsumer (Connection connection, WebSocketSession session) {
        this.rmqConnection = connection;
        this.session = session;
        this.sessionAttributes = this.session.getAttributes();
        this.account = (Account)sessionAttributes.get("account");
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setNotificationConverter(NotificationJsonConverter notificationConverter) {
        this.notificationConverter = notificationConverter;
    }

    public void setNotificationDAO(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void start () throws Exception {
        if (!this.rmqConnection.isOpen() && this.session.isOpen() ) {
            try {
                this.session.close();
            } catch (IOException ex) {}

            return ;
        }

        try {
            this.rmqChannel = this.rmqConnection.createChannel();
            this.queue = this.rmqChannel.queueDeclare().getQueue();
            this.rmqChannel.queueBind(this.queue, this.exchange, "notifications." + this.account.getId());
            this.sendPrevNotifications();
            this.rmqChannel.basicConsume(this.queue, false, this);
        } catch (Exception ex) {
            if (session.isOpen()) session.close();
            if (rmqChannel.isOpen()) rmqChannel.close();
        }
    }
    
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
        if (!session.isOpen()) {
            try {
                this.rmqChannel.close();
            } catch (TimeoutException ex) {}

            return ;
        }

        String eventPayload = new String(body);
        List<Object> eventMessage = List.of("notification", eventPayload);
        this.session.sendMessage(new TextMessage(objectMapper.writeValueAsString(eventMessage)));
        this.rmqChannel.basicAck(envelope.getDeliveryTag(), false);
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
        try {
            if (session.isOpen()) session.close();
        } catch (IOException ex) {}
    }

    private void sendPrevNotifications () throws IOException {
        List<Notification> notifications = this.notificationDAO.selectLatestNotifications(this.account.getId(), 5);

        if (notifications.size() <= 0) return;

        List<Map<String, Object>> notificationsPayload = this.notificationConverter.convert(notifications);
        List<Object> eventMessage = List.of("notifications", notificationsPayload);

        try {
            this.session.sendMessage(new TextMessage(objectMapper.writeValueAsString(eventMessage)));
        } catch (JsonProcessingException ex) {}
    }

    @Override
    public void handleConsumeOk(String consumerTag) {
        // no work to do
    }

    @Override
    public void handleCancelOk(String consumerTag) {
        // no work to do
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
        // no work to do   
    }

    @Override
    public void handleRecoverOk(String consumerTag) {
        // no work to do
    }
}
