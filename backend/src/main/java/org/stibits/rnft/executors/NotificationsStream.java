package org.stibits.rnft.executors;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.Notification;
import org.stibits.rnft.errors.AuthenticationRequiredError;
import org.stibits.rnft.notifications.converters.NotificationJsonConverter;
import org.stibits.rnft.repositories.NotificationDAO;

public class NotificationsStream implements Runnable, Consumer {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private SseEmitter emitter;
    private NotificationDAO notificationDAO;
    private Account account;
    private NotificationJsonConverter notificationJsonConverter;
    private Connection rmqConnection;
    private Channel channel;
    private String exchangeName;
    private String queueName;
    private boolean completed = false;

    public NotificationsStream () {}

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public void setRmqConnection(Connection rmqConnection) {
        this.rmqConnection = rmqConnection;
    }

    public void setNotificationJsonConverter(NotificationJsonConverter notificationJsonConverter) {
        this.notificationJsonConverter = notificationJsonConverter;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setNotificationDAO(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    public void setEmitter(SseEmitter emitter) {
        this.emitter = emitter;
    }

    @Override
    public void run() {
        try {
            if (this.account == null) throw new AuthenticationRequiredError();

            this.sendPrevNotifications();

            this.channel = this.rmqConnection.createChannel();
            this.queueName = this.channel.queueDeclare().getQueue();
            this.channel.queueBind(this.queueName, this.exchangeName, "notifications." + account.getId());
            this.channel.basicConsume(this.queueName, false, this);

            // This makes sure that the runnable doesn't finish running
            while (!this.completed) Thread.sleep(100);
            
        } catch (Throwable ex) {
            System.out.println("[ERROR:NotificationsStream.run] " + ex.getMessage());
            this.closeChannelIfOpen();
            this.emitter.completeWithError(ex);
        }
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
        try {
            Calendar receivedDate = Calendar.getInstance();
            SseEventBuilder eventBuilder = SseEmitter.event().name("notification").data(new String(body));
            this.emitter.send(eventBuilder); 
            this.notificationDAO.updateVuedNotifications(account.getId(), receivedDate);
            this.channel.basicAck(envelope.getDeliveryTag(), false);
        } catch (IOException ex) {
            System.out.println("[ERROR:handleDelivery] " + ex.getMessage());
            this.completed = true;
        }
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
        this.completed = true;
        this.tryComplete();
        this.closeChannelIfOpen();
    }

    private void sendPrevNotifications () throws JsonProcessingException, IOException {
        List<Notification> notifications = this.notificationDAO.selectLatestNotifications(this.account.getId());
        List<Map<String, Object>> data = this.notificationJsonConverter.convert(notifications);
        SseEventBuilder event = SseEmitter.event().data(objectMapper.writeValueAsString(data)).name("notificationsList");

        this.emitter.send(event);
    }

    private void tryComplete () {
        try {
            this.emitter.complete(); 
        } catch (Throwable ex) {}
    }

    private void closeChannelIfOpen () {
        try {
            if (this.channel.isOpen()) this.channel.close();
        } catch (Exception ex) {}
    }

    @Override
    public void handleConsumeOk(String consumerTag) {
        // No work to do
    }

    @Override
    public void handleCancelOk(String consumerTag) {
        // No work to do
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
        // No work to do
    }

    @Override
    public void handleRecoverOk(String consumerTag) {
        // No work to do
    }
}
