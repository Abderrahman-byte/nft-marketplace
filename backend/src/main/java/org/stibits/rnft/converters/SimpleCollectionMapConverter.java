package org.stibits.rnft.converters;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.stibits.rnft.model.bo.NftCollection;

@Component
public class SimpleCollectionMapConverter implements Converter<NftCollection, Map<String, Object>> {
    @Override
    public Map<String, Object> convert(NftCollection source) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", source.getId());
        data.put("name", source.getName());
        data.put("imageUrl", source.getImageUrl());
        data.put("description", source.getDescription());

        return data;
    }
}
