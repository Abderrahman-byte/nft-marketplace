package org.stibits.rnft.notifications;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stibits.rnft.domain.Notification;
import org.stibits.rnft.notifications.converters.NotificationJsonConverter;

@Component
public class NotificationsPublisher {
    private static final Logger logger = LoggerFactory.getLogger(NotificationsPublisher.class);

    @Autowired
    private Connection connection;

    @Value("${rmq.notifications.exchange:notifications}")
    private String notificationExchange;

    @Autowired
    private NotificationJsonConverter notificationConverter;

    @Autowired
    private ObjectMapper objectMapper;

    private Channel channel;

    public void publish (Notification notification) {
        if (!this.channel.isOpen()) return;

        try {
            String routingKey = "notifications." + notification.getTo().getId();
            String body = objectMapper.writeValueAsString(notificationConverter.convert(notification));

            this.channel.basicPublish(notificationExchange, routingKey, null, body.getBytes());
        } catch (Exception ex) {
            logger.error("publish() - " + ex.getMessage(), ex);
        }
    }
    
    @PostConstruct
    private void initialize () throws Exception {
        this.channel = this.connection.createChannel();
        this.channel.exchangeDeclare(notificationExchange, "topic");
    }

    @PreDestroy
    private void closeChannel () throws Exception {
        if(this.channel.isOpen()) this.channel.close();
    }
}
