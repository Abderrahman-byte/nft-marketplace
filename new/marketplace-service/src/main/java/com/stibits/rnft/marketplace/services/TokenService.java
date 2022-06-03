package com.stibits.rnft.marketplace.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stibits.rnft.marketplace.api.TokenDetails;
import com.stibits.rnft.marketplace.domain.Token;
import com.stibits.rnft.marketplace.repositories.TokenRepository;
import com.stibits.rnft.marketplace.web.TokensSortBy;

@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    public List<TokenDetails> getTokensList (TokensSortBy sortBy, int limit, int offset, double maxPrice) {
        List<Token> tokens = List.of();

        if (sortBy.equals(TokensSortBy.HIGH_PRICE)) {
            tokens = tokenRepository.selectTokensSortedByHighPrice(limit, offset, maxPrice);
        } else if (sortBy.equals(TokensSortBy.LOW_PRICE)) {
            tokens = tokenRepository.selectTokensSortedByLowPrice(limit, offset, maxPrice);
        } else if (sortBy.equals(TokensSortBy.POPULARITY)) {
            tokens = tokenRepository.selectTokensSortedByPopularity(limit, offset, maxPrice);
        } else {
            tokens = tokenRepository.selectTokensSortedByLikes(limit, offset, maxPrice);
        }

        return tokens.stream().map(token -> this.getTokenDetails(token)).toList();
    }

    public TokenDetails getTokenDetails (String id) {
        Token token = this.tokenRepository.selectById(id);

        if (token == null) return null;

        return this.getTokenDetails(token);
    }

    public TokenDetails getTokenDetails (Token token) {
        TokenDetails details = new TokenDetails();

        details.setId(token.getId());
        details.setTitle(token.getTitle());
        details.setQuantity(token.getQuantity());
        details.setPreviewUrl(token.getPreviewUrl());
        details.setDescription(token.getDescription());
        details.setCreatorId(token.getCreatorId());
        details.setCreatedDate(token.getCreatedDate());
        details.setForSale(token.getSettings().isForSale());
        details.setInstantSale(token.getSettings().isInstantSale());
        details.setPrice(token.getSettings().getPrice());
        details.setLikesCount(token.getLikes().size());

        return details;
    }
}
