package com.stibits.rnft.marketplace.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stibits.rnft.common.api.ApiSuccessResponse;
import com.stibits.rnft.common.api.ProfileDetails;
import com.stibits.rnft.common.clients.GatewayClient;
import com.stibits.rnft.common.helpers.IpfsService;
import com.stibits.rnft.marketplace.api.CollectionDetails;
import com.stibits.rnft.marketplace.api.TokenDetails;
import com.stibits.rnft.marketplace.domain.Collection;
import com.stibits.rnft.marketplace.domain.Token;
import com.stibits.rnft.marketplace.repositories.CollectionRepository;

@Service
public class CollectionService {
    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private IpfsService ipfsService;

    @Autowired
    private GatewayClient gatewayClient;

    public List<CollectionDetails> getCollectionsList (int limit, int offset) {
        List<Collection> collections = collectionRepository.selectCollectionsByPopularity(limit, offset);

        return collections.stream().map(collection -> this.getCollectionDetails(collection)).toList();
    }

    public CollectionDetails getCollectionDetails (String id) {
        Collection collection = this.collectionRepository.selectCollectionById(id);

        if (collection == null) return null;

        return this.getCollectionDetails(collection);
    }

    public CollectionDetails getCollectionDetails (Collection collection) {
        CollectionDetails details = new CollectionDetails();
        ApiSuccessResponse<ProfileDetails> response = gatewayClient.getUserDetails(collection.getCreatedById());
        List<Token> items = collection.getNfts();

        details.setName(collection.getName());
        details.setId(collection.getId());
        details.setDescription(collection.getDescription());
        details.setImageUrl(ipfsService.resolveHashUrl(collection.getImageUrl()));
        details.setCreatedDate(collection.getCreatedDate());
        details.setTokens(collection.getNfts()
            .subList(0, items.size() <= 3 ? items.size() : 3)
            .stream()
            .map(token -> this.getTokenDetails(token))
            .toList());
        
        if (response != null) details.setCreator(response.getData());

        return details;
    }

    public TokenDetails getTokenDetails (Token token) {
        TokenDetails details = new TokenDetails();

        details.setId(token.getId());
        details.setTitle(token.getTitle());
        details.setPrice(token.getSettings().getPrice());
        details.setForSale(token.getSettings().isForSale());
        details.setInstantSale(token.getSettings().isInstantSale());
        details.setQuantity(token.getQuantity());
        details.setCreatedDate(token.getCreatedDate());
        details.setDescription(token.getDescription());
        details.setPreviewUrl(ipfsService.resolveHashUrl(token.getPreviewUrl()));

        return details;
    }
}
