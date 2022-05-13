package org.stibits.rnft.controllers.notifications;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rabbitmq.client.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.executors.NotificationsStream;
import org.stibits.rnft.notifications.converters.NotificationJsonConverter;
import org.stibits.rnft.repositories.NotificationDAO;

@RestController
@RequestMapping("/api/${api.version}/notifications/sse")
public class NotifcationsStream {
    @Value("${rmq.notifications.exchange:notifications}")
    private String notificationExchange;

    @Autowired
    private Connection connection;

    @Autowired
    private NotificationJsonConverter notificationJsonConverter;

    @Autowired
    private NotificationDAO notificationDAO;

    // This make sure emitter does fall to timeout
    private final long TIMEOUT = Long.MAX_VALUE;

    @GetMapping
    public SseEmitter startStream (@RequestAttribute(name = "account", required = false) Account account) {
        SseEmitter emitter = new SseEmitter(Long.valueOf(TIMEOUT));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        NotificationsStream notificationsStream = new NotificationsStream();
    
        notificationsStream.setAccount(account);
        notificationsStream.setEmitter(emitter);
        notificationsStream.setExchangeName(notificationExchange);
        notificationsStream.setRmqConnection(connection);
        notificationsStream.setNotificationJsonConverter(notificationJsonConverter);
        notificationsStream.setNotificationDAO(notificationDAO);

        executor.execute(notificationsStream);

        return emitter;
    }
}
