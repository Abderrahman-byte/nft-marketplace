package org.stibits.rnft.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.stibits.rnft.model.bo.Account;
import org.stibits.rnft.model.bo.NFToken;

@Component
public class TokenMapConverter implements Converter<NFToken, Map<String, Object>> {
    @Autowired
    public ProfileDetailsConverter profileDetailsConverter;

    @Autowired
    public SimpleCollectionMapConverter collectionMapConverter;

    public List<Map<String, Object>> convertList (List<NFToken> nfts, Account account) {
        return nfts.stream().map(nft -> this.convert(nft, account)).toList();
    }

    public List<Map<String, Object>> convertList (List<NFToken> nfts) {
        return nfts.stream().map(nft -> this.convert(nft)).toList();
    }

    @Override
    public Map<String, Object> convert(NFToken source) {
        Map<String, Object> data = new HashMap<>();

        data.put("id", source.getId());
        data.put("title", source.getTitle());
        data.put("previewUrl", source.getPreviewUrl());
        data.put("price", source.getPrice());
        data.put("description", source.getDescription());
        data.put("likesCount", source.getLikes().size());

        if (source.getCollection() != null) {
            data.put("collection", collectionMapConverter.convert(source.getCollection()));
        }

        if (source.getCreator().getProfile() != null) {
            data.put("creator", profileDetailsConverter.convert(source.getCreator().getProfile()));
        } else {
            data.put("creatorId", source.getCreator().getId());
        }
        
        if (source.getOwner().getProfile() != null) {
            data.put("owner", profileDetailsConverter.convert(source.getOwner().getProfile()));
        } else {
            data.put("ownerId", source.getOwner().getId());
        }

        return data;
    }

    public Map<String, Object> convert(NFToken source , Account account) {
        Map<String, Object> data = this.convert(source);

        if (account != null)
            data.put("liked", source.getLikes().stream().filter(like -> like.getAccountId().equals(account.getId())).count() > 0);

        return data;
    }
}
