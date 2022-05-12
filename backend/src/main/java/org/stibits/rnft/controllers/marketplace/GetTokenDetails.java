package org.stibits.rnft.controllers.marketplace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.stibits.rnft.converters.BidMapConverter;
import org.stibits.rnft.converters.TokenMapConverter;
import org.stibits.rnft.converters.TransactionMapConverter;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.Bid;
import org.stibits.rnft.entities.Token;
import org.stibits.rnft.entities.Transaction;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.TokenNotFound;
import org.stibits.rnft.repositories.BidsDAO;
import org.stibits.rnft.repositories.NFTokenDAO;
import org.stibits.rnft.repositories.TransactionDAO;

@RestController
@RequestMapping("/api/${api.version}/marketplace/tokens/{id}")
public class GetTokenDetails {
    @Autowired
    private NFTokenDAO nftokenDAO;

    @Autowired
    private TokenMapConverter tokenMapConverter;

    @Autowired
    private BidsDAO bidsDAO;

    @Autowired
    private TransactionDAO transactionDAO;

    @Autowired
    private BidMapConverter bidMapConverter;

    @Autowired
    private TransactionMapConverter transactionConverter;

    @GetMapping
    public Map<String, Object> handleGetRequest(
            @ModelAttribute(name = "id") String tokenId,
            @RequestAttribute(name = "account", required = false) Account account) throws ApiError {
        
        Map<String, Object> response = new HashMap<>();
        Token token = nftokenDAO.selectTokenById(tokenId);
        if (token == null) throw new TokenNotFound();

        Map<String, Object> data = tokenMapConverter.convert(token, account);
        Bid highestBid = bidsDAO.getTokenHighestBid(tokenId);

        if (highestBid != null) data.put("highestBid", bidMapConverter.convert(highestBid));

        response.put("success", true);
        response.put("data", data);

        return response;
    }

    @GetMapping("/bids")
    public Map<String, Object> getBidsList(
            @ModelAttribute(name = "id") String tokenId,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "offset", defaultValue = "0") int offset) throws ApiError {

        Map<String, Object> response = new HashMap<>();
        Token token = nftokenDAO.selectTokenById(tokenId);

        if (token == null) throw new TokenNotFound();

        if (limit <= 0) limit = 10;

        if (offset < 0) offset = 0;

        List<Bid> bidsList = bidsDAO.selectBidsByTokenId(tokenId, limit, offset);

        response.put("success", true);
        response.put("data", bidMapConverter.convert(bidsList));

        return response;
    }

    @GetMapping("/transactions")
    public Map<String, Object> getTransactionsList (
        @ModelAttribute(name = "id") String tokenId,
        @RequestParam(name = "limit", defaultValue = "10") int limit,
        @RequestParam(name = "offset", defaultValue = "0") int offset) throws ApiError {

        Map<String, Object> response = new HashMap<>();

        Token token = nftokenDAO.selectTokenById(tokenId);

        if (token == null) throw new TokenNotFound();

        if (limit <= 0) limit = 10;

        if (offset < 0) offset = 0;

        List<Transaction> transactions = this.transactionDAO.selectTransactionsByTokenId(tokenId, limit, offset);

        response.put("success", true);
        response.put("data", this.transactionConverter.convert(transactions));

        return response;
    }

    @ModelAttribute
    public void tokenIdModel(@PathVariable(name = "id", required = false) String tokenId, Model model) throws ApiError {
        if (tokenId == null || tokenId.equals("")) throw new TokenNotFound();

        model.addAttribute("id", tokenId);
    }
}
