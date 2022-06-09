package com.stibits.rnft.commonweb.streams;

import java.io.IOException;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stibits.rnft.common.api.ApiFailureResponse;
import com.stibits.rnft.common.errors.ApiError;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractStreamRunnable implements Runnable {
    @Setter protected SseEmitter sseEmitter;
    @Setter protected ObjectMapper objectMapper;
    private volatile boolean completed;

    @Override
    public void run() {
        try {
            this.execute();
        } catch (Exception ex) {
            log.error("ERROR : " + ex.getMessage(), ex);
            this.sseEmitter.completeWithError(ex);
        }
    }

    protected void sendErrorAndComplete (ApiError error) throws IOException {
        this.sendDataEvent("error", new ApiFailureResponse<>(error));
        this.sseEmitter.complete();
    }

    protected void sendDataEvent (String event, Object data) throws IOException {
        if (this.getCompleted()) return ;
        this.sseEmitter.send(this.makeDataEvent(event, data));
    }
    
    protected SseEventBuilder makeDataEvent (String event, Object data) throws JsonProcessingException {
        String dataJson = this.objectMapper.writeValueAsString(data);
        return SseEmitter.event().name(event).data(dataJson);
    }

    public synchronized void setCompleted (boolean completed) {
        this.completed = completed;
    }

    public synchronized boolean getCompleted () {
        return this.completed;
    }

    protected abstract void execute () throws Exception;
    public abstract void close () throws Exception;
    public abstract void complete ();
}
