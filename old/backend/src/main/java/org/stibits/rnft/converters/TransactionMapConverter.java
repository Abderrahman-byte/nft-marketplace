package org.stibits.rnft.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.stibits.rnft.domain.Transaction;

@Component
public class TransactionMapConverter implements Converter<Transaction, Map<String, Object>> {
    @Autowired
    private ProfileDetailsConverter profileDetailsConverter;

    @Autowired
    private DateTimestampConverter timestampConverter;

    @Override
    public Map<String, Object> convert(Transaction source) {
        Map<String, Object> data = new HashMap<>();

        data.put("id", source.getId());
        data.put("price", source.getPrice());
        data.put("from", profileDetailsConverter.convert(source.getFrom()));
        data.put("to", profileDetailsConverter.convert(source.getTo()));
        data.put("createdDate", timestampConverter.convert(source.getCreatedDate()));

        return data;
    }

    public List<Map<String, Object>> convert (List<Transaction> source) {
        return source.stream().map(transaction -> this.convert(transaction)).toList();
    }
}
