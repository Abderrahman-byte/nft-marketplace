package com.stibits.rnft.marketplace.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stibits.rnft.common.api.ApiSuccessResponse;
import com.stibits.rnft.common.errors.ApiError;
import com.stibits.rnft.marketplace.api.BidDetails;
import com.stibits.rnft.marketplace.api.TokenDetails;
import com.stibits.rnft.marketplace.api.TransactionDetails;
import com.stibits.rnft.marketplace.errors.TokenNotFoundError;
import com.stibits.rnft.marketplace.services.BidService;
import com.stibits.rnft.marketplace.services.TokenService;
import com.stibits.rnft.marketplace.services.TransactionService;

@RestController
@RequestMapping("/tokens")
public class TokensController {
    // TODO : check if user likes the tokens
    
    @Autowired
    private TokenService tokenService;

    @Autowired
    private BidService bidService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ApiSuccessResponse<List<TokenDetails>> getTokensList(
            @RequestParam(name = "sort", required = false, defaultValue = "LIKES") TokensSortBy sortBy,
            @RequestParam(name = "range", required = false, defaultValue = "100000000000") double maxPrice,
            @RequestParam(name = "limit", required = false, defaultValue = "50") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset) {

        if (limit <= 0) limit = 50;
        if (offset < 0) offset = 0;

        return new ApiSuccessResponse<>(tokenService.getTokensList(sortBy, limit, offset, maxPrice));
    }

    @GetMapping("/{id}")
    public ApiSuccessResponse<TokenDetails> getTokenDetails (@PathVariable String id) throws ApiError {
        TokenDetails tokenDetails = tokenService.getTokenDetails(id);

        if (tokenDetails == null) throw new TokenNotFoundError();

        return new ApiSuccessResponse<>(tokenDetails);
    }

    @GetMapping("/{id}/bids")
    public ApiSuccessResponse<List<BidDetails>> getTokenBids(
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @PathVariable String id) {

        if (limit <= 0) limit = 10;
        if (offset < 0) offset = 0;
        
        return new ApiSuccessResponse<>(bidService.getTokenBids(id, limit, offset));
    }

    @GetMapping("/{id}/transactions")
    public ApiSuccessResponse<List<TransactionDetails>> getTokenTransactions(
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @PathVariable String id) {

        if (limit <= 0) limit = 10;
        if (offset < 0) offset = 0;
        
        return new ApiSuccessResponse<>(transactionService.getTokenTransactions(id, limit, offset));
    }
}
