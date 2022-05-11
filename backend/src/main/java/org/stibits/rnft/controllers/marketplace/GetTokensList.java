package org.stibits.rnft.controllers.marketplace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.stibits.rnft.converters.BidMapConverter;
import org.stibits.rnft.converters.TokenMapConverter;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.Bid;
import org.stibits.rnft.entities.Token;
import org.stibits.rnft.repositories.BidsDAO;
import org.stibits.rnft.repositories.NFTokenDAO;

// FIXME : this request take long time to respond
// FIXME : maybe use enum as a type of sortby param

@RestController
@RequestMapping("/api/${api.version}/marketplace/tokens")
public class GetTokensList {
    @Autowired
    private NFTokenDAO nftokenDAO;

    @Autowired
    private BidsDAO bidsDAO;

    @Autowired
    private TokenMapConverter converter;

    @Autowired
    private BidMapConverter bidMapConverter;

    @GetMapping
    public Map<String, Object> handleGetRequest (
            @RequestParam(name = "sort", required = false, defaultValue = "LIKES") String sortBy, 
            @RequestParam(name="range", required = false, defaultValue = "100000") double maxPrice, 
            @RequestParam(name="limit", required = false, defaultValue = "50") int limit,
            @RequestParam(name="offset", required = false, defaultValue = "0") int offset,
            @RequestAttribute(name = "account", required = false) Account account) {
        
        if (!List.of("LIKES", "HIGH_PRICE", "LOW_PRICE").contains(sortBy)) sortBy = "LIKES";

        Map<String, Object> response = new HashMap<>();
        List<Token> tokens = new ArrayList<>();

        if (limit <= 0) limit = 50;
        if (offset < 0) offset = 0;
        
        if (sortBy.equals("HIGH_PRICE")) tokens = nftokenDAO.selectTokensSortedByHighPrice(limit, offset, maxPrice);
        else if (sortBy.equals("LOW_PRICE")) tokens = nftokenDAO.selectTokensSortedByLowPrice(limit, offset, maxPrice);
        else tokens = nftokenDAO.selectTokensSortedByLikes(limit, offset, maxPrice);

        List<Map<String, Object>> tokensData = converter.convertList(tokens, account).stream().map(data -> {
            Bid bid = bidsDAO.getTokenHighestBid((String)data.get("id"));
            
            if (bid != null) data.put("highestBid", bidMapConverter.convert(bid));

            return data;
        }).toList();

        response.put("success", true);
        response.put("data", tokensData);

        return response;
    }
    
}
