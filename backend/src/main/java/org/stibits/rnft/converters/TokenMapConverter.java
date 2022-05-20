package org.stibits.rnft.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.stibits.rnft.domain.Account;
import org.stibits.rnft.domain.Token;

@Component
public class TokenMapConverter implements Converter<Token, Map<String, Object>> {
    @Autowired
    public ProfileDetailsConverter profileDetailsConverter;

    @Autowired
    public SimpleCollectionMapConverter collectionMapConverter;

    @Autowired
    public DateTimestampConverter timestampConverter;

    public List<Map<String, Object>> convertList (List<Token> nfts, Account account) {
        return nfts.stream().map(nft -> this.convert(nft, account)).toList();
    }

    public List<Map<String, Object>> convertList (List<Token> nfts) {
        return nfts.stream().map(nft -> this.convert(nft)).toList();
    }

    @Override
    public Map<String, Object> convert(Token source) {
        Map<String, Object> data = new HashMap<>();

        data.put("id", source.getId());
        data.put("title", source.getTitle());
        data.put("previewUrl", source.getPreviewUrl());
        data.put("description", source.getDescription());
        data.put("likesCount", source.getLikes().size());
        data.put("isForSale", source.getSettings().isForSale());
        data.put("instantSale", source.getSettings().isInstantSale());
        data.put("price", source.getSettings().getPrice());
        data.put("createdDate", timestampConverter.convert(source.getCreatedDate()));
    
        if (source.getCollection() != null) {
            data.put("collection", collectionMapConverter.convert(source.getCollection()));
        }

        if (source.getCreator().getProfile() != null) {
            data.put("creator", profileDetailsConverter.convert(source.getCreator().getProfile()));
        } else {
            data.put("creatorId", source.getCreator().getId());
        }
        
        if (source.getOwner() != null && source.getOwner().getProfile() != null) {
            data.put("owner", profileDetailsConverter.convert(source.getOwner().getProfile()));
        }

        return data;
    }

    public Map<String, Object> convert(Token source , Account account) {
        Map<String, Object> data = this.convert(source);

        if (account != null)
            data.put("liked", source.getLikes().stream().filter(like -> like.getAccountId().equals(account.getId())).count() > 0);

        return data;
    }
}
