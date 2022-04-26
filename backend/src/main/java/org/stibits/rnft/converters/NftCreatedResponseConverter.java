package org.stibits.rnft.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.stibits.rnft.entities.NFToken;

@Component
public class NftCreatedResponseConverter implements Converter<NFToken, Map<String, Object>> {
    @Override
    public Map<String, Object> convert(NFToken source) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        
        data.put("id", source.getId());
        data.put("contentUrl", source.getPreviewUrl());
        response.put("data", data);
        response.put("success", true);

        return response;
    }
    
    public Map<String, Object> convert (List<NFToken> source, String contentUrl) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        data.put("contentUrl", contentUrl);
        data.put("items", source.stream().map(nft -> {
            Map<String, String> item = new HashMap<>();
            item.put("id", nft.getId());
            item.put("title", nft.getTitle());

            return item;
        }).toList());

        response.put("data", data);
        response.put("success", true);
        
        return response;
    }
}