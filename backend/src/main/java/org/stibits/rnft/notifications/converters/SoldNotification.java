package org.stibits.rnft.notifications.converters;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.stibits.rnft.converters.DateTimestampConverter;
import org.stibits.rnft.converters.ProfileDetailsConverter;
import org.stibits.rnft.converters.SimpleTokenMapConverter;
import org.stibits.rnft.entities.Transaction;

@Component
public class SoldNotification implements Converter<Transaction, Map<String, Object>> {
    @Autowired
    private ProfileDetailsConverter profileDetailsConverter;

    @Autowired
    private SimpleTokenMapConverter tokenMapConverter;

    @Autowired
    private DateTimestampConverter timestampConverter;

    @Override
    public Map<String, Object> convert(Transaction source) {
        Map<String, Object> data = new HashMap<>();

        data.put("id", source.getId());
        data.put("to", profileDetailsConverter.convert(source.getTo()));
        data.put("price", source.getPrice());
        data.put("token", tokenMapConverter.convert(source.getToken()));
        data.put("createdDate", timestampConverter.convert(source.getCreatedDate()));

        return data;
    }
}
