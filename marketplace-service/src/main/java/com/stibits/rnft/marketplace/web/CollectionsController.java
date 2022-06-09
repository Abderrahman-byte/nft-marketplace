package com.stibits.rnft.marketplace.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stibits.rnft.common.api.ApiResponse;
import com.stibits.rnft.common.api.ApiSuccessResponse;
import com.stibits.rnft.common.errors.ApiError;
import com.stibits.rnft.marketplace.api.CollectionDetails;
import com.stibits.rnft.marketplace.errors.CollectionNotFoundError;
import com.stibits.rnft.marketplace.services.CollectionService;
import com.stibits.rnft.marketplace.services.TokenService;

@RestController
@RequestMapping("/collections")
public class CollectionsController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private CollectionService collectionService;

    @GetMapping
    public ApiResponse getCollections (
        @RequestParam(name = "limit", required = false, defaultValue = "50") int limit,
        @RequestParam(name = "offset", required = false, defaultValue = "0") int offset) {
        
        if (limit <= 0) limit = 10;
        if (offset < 0) offset = 0;

        return new ApiSuccessResponse<>(collectionService.getCollectionsList(limit, offset));
    }

    @GetMapping("/{id}")
    public ApiResponse getCollectionDetais (@PathVariable String id) throws ApiError {
        CollectionDetails details = this.collectionService.getCollectionDetails(id);

        if (details == null) throw new CollectionNotFoundError();

        return new ApiSuccessResponse<>(details);
    }

    @GetMapping("/{id}/items")
    public ApiResponse getCollectionItems (
        @PathVariable String id,
        @RequestParam(name = "sort", required = false, defaultValue = "LIKES") TokensSortBy sortBy, 
        @RequestParam(name="range", required = false, defaultValue = "100000") double maxPrice, 
        @RequestParam(name="limit", required = false, defaultValue = "50") int limit,
        @RequestParam(name="offset", required = false, defaultValue = "0") int offset) throws ApiError {
        
        if (limit <= 0) limit = 10;
        if (offset < 0) offset = 0;
        
        return new ApiSuccessResponse<>(this.tokenService.getTokensList(null, id, sortBy, limit, offset, maxPrice));
    }
}
