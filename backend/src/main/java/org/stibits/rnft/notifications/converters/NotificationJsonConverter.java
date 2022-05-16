package org.stibits.rnft.notifications.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.stibits.rnft.converters.DateTimestampConverter;
import org.stibits.rnft.entities.Notification;

@Component
public class NotificationJsonConverter implements Converter<Notification, Map<String, Object>> {
    @Autowired
    private DateTimestampConverter timestampConverter;

    @Override
    public Map<String, Object> convert(Notification source) {
        Map<String, Object> data = new HashMap<>();

        data.put("id", source.getId());
        data.put("event", source.getEvent().toString());
        data.put("createdDate", timestampConverter.convert(source.getCreatedDate()));
        data.put("vued", source.isVued());
        data.put("details", source.getMetadata());

        return data;
    }

    public List<Map<String, Object>> convert (List<Notification> notifications) {
        return notifications.stream().map(notification -> this.convert(notification)).toList();
    }
}
