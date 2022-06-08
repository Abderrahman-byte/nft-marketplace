package com.stibits.rnft.marketplace.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stibits.rnft.common.api.ApiSuccessResponse;
import com.stibits.rnft.common.api.ProfileDetails;
import com.stibits.rnft.common.clients.GatewayClient;
import com.stibits.rnft.common.errors.ApiError;
import com.stibits.rnft.common.helpers.IpfsService;
import com.stibits.rnft.marketplace.api.CollectionDetails;
import com.stibits.rnft.marketplace.api.CreateTokenMetadata;
import com.stibits.rnft.marketplace.api.TokenDetails;
import com.stibits.rnft.marketplace.domain.Collection;
import com.stibits.rnft.marketplace.domain.Token;
import com.stibits.rnft.marketplace.repositories.TokenRepository;
import com.stibits.rnft.marketplace.web.TokensSortBy;

@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private IpfsService ipfsService;

    @Autowired
    private GatewayClient gatewayClient;

    public Token createToken (CreateTokenMetadata metadata, Collection collection, String assetName, String preview, String ipfs, String creatorId) throws ApiError {
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
        token.setCollection(collection);

        return this.tokenRepository.save(token);
    }

    public List<TokenDetails> getTokensList (ProfileDetails account, TokensSortBy sortBy, int limit, int offset, double maxPrice) {
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

        return tokens.stream().map(token -> this.getTokenDetails(token, account)).toList();
    }

    public List<TokenDetails> getTokensList (ProfileDetails account, String collectionId, TokensSortBy sortBy, int limit, int offset, double maxPrice) {
        List<Token> tokens = List.of();

        if (sortBy.equals(TokensSortBy.HIGH_PRICE)) {
            tokens = tokenRepository.selectTokensSortedByHighPrice(collectionId, limit, offset, maxPrice);
        } else if (sortBy.equals(TokensSortBy.LOW_PRICE)) {
            tokens = tokenRepository.selectTokensSortedByLowPrice(collectionId, limit, offset, maxPrice);
        } else if (sortBy.equals(TokensSortBy.POPULARITY)) {
            tokens = tokenRepository.selectTokensSortedByPopularity(collectionId, limit, offset, maxPrice);
        } else {
            tokens = tokenRepository.selectTokensSortedByLikes(collectionId, limit, offset, maxPrice);
        }

        return tokens.stream().map(token -> this.getTokenDetails(token, account)).toList();
    }

    public TokenDetails getTokenDetails (String id, ProfileDetails account) {
        Token token = this.tokenRepository.selectById(id);

        if (token == null) return null;

        return this.getTokenDetails(token, account);
    }

    // TODO : get token owner
    // TODO : get token higest bid
    public TokenDetails getTokenDetails (Token token, ProfileDetails account) {
        ApiSuccessResponse<ProfileDetails> response = gatewayClient.getUserDetails(token.getCreatorId());
        TokenDetails details = new TokenDetails();

        details.setId(token.getId());
        details.setTitle(token.getTitle());
        details.setQuantity(token.getQuantity());
        details.setPreviewUrl(ipfsService.resolveHashUrl(token.getPreviewUrl()));
        details.setDescription(token.getDescription());
        details.setCreatedDate(token.getCreatedDate());
        details.setForSale(token.getSettings().isForSale());
        details.setInstantSale(token.getSettings().isInstantSale());
        details.setPrice(token.getSettings().getPrice());
        details.setLikesCount(token.getLikes().size());
        details.setCreator(response.getData());

        if (account != null ) {
            details.setLiked(token.getLikes().stream().anyMatch(like -> like.getAccountId().equals(account.getId())));
        }

        if (token.getCollection() != null) 
            details.setCollection(this.getCollectionSimpleDetails(token.getCollection()));

        return details;
    }

    public CollectionDetails getCollectionSimpleDetails (Collection collection) {
        CollectionDetails details = new CollectionDetails();

        details.setId(collection.getId());
        details.setCreatedDate(collection.getCreatedDate());
        details.setName(collection.getName());
        details.setImageUrl(ipfsService.resolveHashUrl(collection.getImageUrl()));
        details.setDescription(collection.getDescription());

        return details;
    }
}
