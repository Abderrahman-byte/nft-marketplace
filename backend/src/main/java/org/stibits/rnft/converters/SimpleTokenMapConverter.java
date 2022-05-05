package org.stibits.rnft.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.NFToken;

@Component
public class SimpleTokenMapConverter implements Converter<NFToken, Map<String, Object>>  {
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

        return data;
    }

    public Map<String, Object> convert(NFToken source , Account account) {
        Map<String, Object> data = this.convert(source);

        if (account != null)
            data.put("liked", source.getLikes().stream().filter(like -> like.getAccountId().equals(account.getId())).count() > 0);

        return data;
    }   
}
