package org.stibits.rnft.converters;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.stibits.rnft.model.bo.NftCollection;

@Component
public class CollectionMapConverter implements Converter<NftCollection, Map<String, Object>> {
    @Autowired
    public ProfileDetailsConverter profileDetailsConverter;

    @Override
    public Map<String, Object> convert(NftCollection source) {
        Map<String, Object> data = new HashMap<>();

        data.put("id", source.getId());
        data.put("name", source.getName());
        data.put("imageUrl", source.getImageUrl());
        data.put("description", source.getDescription());
        data.put("itemsCount", source.getNfts().size());
        data.put("totalPrice", source.getNfts().stream().reduce(0.0, (subtotal, token) -> subtotal + token.getPrice(), Double::sum));

        if (source.getCreatedBy().getProfile() != null) {
            data.put("creator", profileDetailsConverter.convert(source.getCreatedBy().getProfile()));
        }

        return data;
    }
}
