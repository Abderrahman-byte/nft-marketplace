package com.stibits.rnft.marketplace.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stibits.rnft.common.api.ApiResponse;
import com.stibits.rnft.common.api.ApiSuccessResponse;
import com.stibits.rnft.common.api.ProfileDetails;
import com.stibits.rnft.common.errors.ApiError;
import com.stibits.rnft.marketplace.api.BidDetails;
import com.stibits.rnft.marketplace.api.UpdateSettingsRequest;
import com.stibits.rnft.marketplace.domain.Token;
import com.stibits.rnft.marketplace.api.TokenDetails;
import com.stibits.rnft.marketplace.api.TransactionDetails;
import com.stibits.rnft.marketplace.errors.TokenNotFoundError;
import com.stibits.rnft.marketplace.repositories.LikeRepository;
import com.stibits.rnft.marketplace.repositories.TokenRepository;
import com.stibits.rnft.marketplace.services.BidService;
import com.stibits.rnft.marketplace.services.TokenService;
import com.stibits.rnft.marketplace.services.TransactionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/tokens")
@Slf4j
public class TokensController {
    // TODO : check if user likes the tokens
    
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BidService bidService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ApiSuccessResponse<List<TokenDetails>> getTokensList(
            @RequestParam(name = "sort", required = false, defaultValue = "LIKES") TokensSortBy sortBy,
            @RequestParam(name = "range", required = false, defaultValue = "100000000000") double maxPrice,
            @RequestParam(name = "limit", required = false, defaultValue = "50") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @AuthenticationPrincipal ProfileDetails account) {

        if (limit <= 0) limit = 50;
        if (offset < 0) offset = 0;

        return new ApiSuccessResponse<>(tokenService.getTokensList(account, sortBy, limit, offset, maxPrice));
    }

    @GetMapping("/{id}")
    public ApiSuccessResponse<TokenDetails> getTokenDetails (@PathVariable String id, @AuthenticationPrincipal ProfileDetails account) throws ApiError {
        TokenDetails tokenDetails = tokenService.getTokenDetails(id, account);

        if (tokenDetails == null) throw new TokenNotFoundError();

        return new ApiSuccessResponse<>(tokenDetails);
    }

    @PutMapping("/{id}")
    public ApiResponse updateTokenSettings (@PathVariable String id, @AuthenticationPrincipal ProfileDetails account, @RequestBody UpdateSettingsRequest request) throws ApiError {
        if (account == null) return ApiResponse.getFailureResponse();

        Token token = this.tokenRepository.selectById(id);

        if (token == null) throw new TokenNotFoundError();

        String ownerId = this.transactionService.getTokenOwner(token);

        if (!ownerId.equals(account.getId())) throw new AccessDeniedException("You dont own this token.");

        log.info("request => " + request);
        log.info("Is for sale : " + request.getIsForSale());

        if (request.getPrice() != null) token.getSettings().setPrice(request.getPrice());
        if (request.getInstantSale() != null) token.getSettings().setInstantSale(request.getInstantSale());
        if (request.getIsForSale() != null) token.getSettings().setForSale(request.getIsForSale());

        this.tokenRepository.save(token);

        return ApiResponse.getFailureResponse();
    }

    @PostMapping("/{id}/like")
    public ApiResponse likeToken (@PathVariable String id, @AuthenticationPrincipal ProfileDetails profile) {
        if (profile == null) throw new AccessDeniedException("Unauthenticated");

        try {
            boolean created = this.likeRepository.likeToken(id, profile.getId());
            
            return new ApiResponse() {
                public boolean isSuccess() {
                    return created;
                }
            };
        } catch (DataIntegrityViolationException ex) {
            return ApiResponse.getFailureResponse();   
        }
    }

    @DeleteMapping("/{id}/like")
    public ApiResponse unlikeToken (@PathVariable String id, @AuthenticationPrincipal ProfileDetails profile) {
        if (profile == null) throw new AccessDeniedException("Unauthenticated");

        try {
            boolean deleted = this.likeRepository.unlikeToken(id, profile.getId());
            
            return new ApiResponse() {
                public boolean isSuccess() {
                    return deleted;
                }
            };
        } catch (DataIntegrityViolationException ex) {
            return ApiResponse.getFailureResponse();   
        }
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
