package com.stibits.rnft.marketplace.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stibits.rnft.common.helpers.IpfsService;
import com.stibits.rnft.marketplace.api.CreateTokenMetadata;
import com.stibits.rnft.marketplace.api.TokenDetails;
import com.stibits.rnft.marketplace.domain.Token;
import com.stibits.rnft.marketplace.repositories.TokenRepository;
import com.stibits.rnft.marketplace.web.TokensSortBy;

@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private IpfsService ipfsService;

    public Token createToken (CreateTokenMetadata metadata, String assetName, String preview, String ipfs, String creatorId) {
        Token token = new Token();

        token.setTitle(metadata.getTitle());
        token.setDescription(metadata.getDescription());
        token.getSettings().setForSale(metadata.isForSale() || metadata.isInstantSale());
        token.getSettings().setInstantSale(metadata.isInstantSale());
        token.getSettings().setPrice(metadata.getPrice().doubleValue());
        token.setAssetPath(assetName);
        token.setIpfs(ipfs);
        token.setPreviewUrl(preview);
        token.setCreatorId(creatorId);

        return this.tokenRepository.save(token);
    }

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
        details.setPreviewUrl(ipfsService.resolveHashUrl(token.getPreviewUrl()));
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
