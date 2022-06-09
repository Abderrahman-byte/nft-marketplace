package com.stibits.rnft.marketplace.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stibits.rnft.marketplace.api.BidDetails;
import com.stibits.rnft.marketplace.domain.Bid;
import com.stibits.rnft.marketplace.repositories.BidRepository;

@Service
public class BidService {
    @Autowired
    private BidRepository bidRepository;

    public List<BidDetails> getTokenBids (String tokenId, int limit, int offset) {
        return this.bidRepository
            .getBidsOfToken(tokenId, limit, offset)
            .stream()
            .map(bid -> this.getBidDetails(bid))
            .toList();
    }

    public BidDetails getBidDetails (Bid bid) {
        BidDetails details = new BidDetails();

        details.setId(bid.getId());
        details.setPrice(bid.getPrice());
        details.setResponse(bid.getResponse().name());
        details.setFromId(bid.getFromId());
        details.setToId(bid.getToId());
        details.setCreatedDate(bid.getCreatedDate());

        return details;
    }
}
