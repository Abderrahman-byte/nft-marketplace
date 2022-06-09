package com.stibits.rnft.marketplace.utils;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

@Component
public class BasicRmqPublisher {
    @Autowired
    private Connection rmqConnection;

    private Channel rmqChannel;

    @PostConstruct
    public void init () {
        try {
            this.rmqChannel = this.rmqConnection.createChannel();
        } catch (IOException ex) {
            this.rmqChannel = null;
        }
    }

    public boolean publish (String exchange, String routingKey, byte body[]) {
        try {
            this.rmqChannel.basicPublish(exchange, routingKey, true, null, body);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
