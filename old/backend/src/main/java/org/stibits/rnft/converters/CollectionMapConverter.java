package org.stibits.rnft.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.stibits.rnft.domain.NftCollection;
import org.stibits.rnft.domain.Token;

@Component
public class CollectionMapConverter implements Converter<NftCollection, Map<String, Object>> {
    @Autowired
    public ProfileDetailsConverter profileDetailsConverter;

    @Autowired
    public SimpleTokenMapConverter tokenMapConverter;

    private int itemsPreviewCount = 8;

    @Override
    public Map<String, Object> convert(NftCollection source) {
        Map<String, Object> data = this.convertSimple(source);
        List<Token> items = source.getNfts();

        data.put("itemsCount", items.size());
        data.put("totalPrice", items.stream().reduce(0.0, (subtotal, token) -> subtotal + token.getSettings().getPrice(), Double::sum));

        if (source.getCreatedBy().getProfile() != null) {
            data.put("creator", profileDetailsConverter.convert(source.getCreatedBy().getProfile()));
        } else {
            data.put("createdId", source.getCreatedBy().getId());
        }

        data.put("items", tokenMapConverter.convertList(items.subList(0, items.size() >= itemsPreviewCount ? itemsPreviewCount : items.size())));

        return data;
    }

    public Map<String, Object> convertSimple (NftCollection source) {
        Map<String, Object> data = new HashMap<>();

        data.put("id", source.getId());
        data.put("name", source.getName());
        data.put("imageUrl", source.getImageUrl());
        data.put("description", source.getDescription());

        return data;
    }

    public List<Map<String, Object>> convertSimple (List<NftCollection> source) {
        return source.stream().map(collection -> this.convertSimple(collection)).toList();
    }

    public List<Map<String, Object>> convert (List<NftCollection> source) {
        return source.stream().map(collection -> this.convert(collection)).toList();
    }
}