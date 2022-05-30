package org.stibits.rnft.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.stibits.rnft.domain.Account;
import org.stibits.rnft.domain.Token;

@Component
public class SimpleTokenMapConverter implements Converter<Token, Map<String, Object>>  {
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
        data.put("price", source.getSettings().getPrice());
        data.put("description", source.getDescription());
        data.put("likesCount", source.getLikes().size());
        data.put("isForSale", source.getSettings().isForSale());
        data.put("instantSale", source.getSettings().isInstantSale());

        return data;
    }

    public Map<String, Object> convert(Token source , Account account) {
        Map<String, Object> data = this.convert(source);

        if (account != null)
            data.put("liked", source.getLikes().stream().filter(like -> like.getAccountId().equals(account.getId())).count() > 0);

        return data;
    }   
}
