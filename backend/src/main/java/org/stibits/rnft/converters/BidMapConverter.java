package org.stibits.rnft.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.stibits.rnft.entities.Bid;

@Component
public class BidMapConverter implements Converter<Bid, Map<String, Object>> {
    @Autowired
    private ProfileDetailsConverter profileConverter;

    @Autowired
    private DateTimestampConverter timestampConverter;

    @Override
    public Map<String, Object> convert(Bid source) {
        Map<String, Object> data = new HashMap<>();

        data.put("id", source.getId());
        data.put("price", source.getPrice());
        data.put("createdDate", timestampConverter.convert(source.getCreateDate()));
        data.put("response", source.getResponse().toString());
        data.put("from", profileConverter.convert(source.getFrom().getProfile()));
        data.put("tokenId", source.getToken().getId());
        
        return data;
    }

    public List<Map<String, Object>> convert (List<Bid> source) {
        return source.stream().map(bid -> this.convert(bid)).toList();
    }
}
