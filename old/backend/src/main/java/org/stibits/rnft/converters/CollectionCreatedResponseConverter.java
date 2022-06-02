package org.stibits.rnft.converters;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.stibits.rnft.domain.NftCollection;

@Component
public class CollectionCreatedResponseConverter implements Converter<NftCollection, Map<String, Object>> {
    @Override
    public Map<String, Object> convert(NftCollection source) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> data = new HashMap<>();
        
        data.put("id", source.getId());
        data.put("imageUrl", source.getImageUrl());
        response.put("data", data);
        response.put("success", true);

        return response;
    }
}
