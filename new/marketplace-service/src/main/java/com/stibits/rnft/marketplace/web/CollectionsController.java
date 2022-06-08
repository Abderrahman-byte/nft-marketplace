package com.stibits.rnft.marketplace.web;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stibits.rnft.common.api.ApiResponse;
import com.stibits.rnft.common.api.ApiSuccessResponse;
import com.stibits.rnft.common.api.ProfileDetails;
import com.stibits.rnft.common.errors.ApiError;
import com.stibits.rnft.common.errors.DuplicationError;
import com.stibits.rnft.common.errors.UnexpectedMediaType;
import com.stibits.rnft.common.helpers.IpfsService;
import com.stibits.rnft.common.utils.FileTypeService;
import com.stibits.rnft.marketplace.api.CollectionDetails;
import com.stibits.rnft.marketplace.api.CreateCollectionMetadata;
import com.stibits.rnft.marketplace.domain.Collection;
import com.stibits.rnft.marketplace.errors.CollectionNotFoundError;
import com.stibits.rnft.marketplace.repositories.CollectionRepository;
import com.stibits.rnft.marketplace.services.CollectionService;
import com.stibits.rnft.marketplace.services.TokenService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/collections")
@Slf4j
public class CollectionsController {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IpfsService ipfsService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private FileTypeService fileTypeService;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private CollectionService collectionService;

    private Pattern imageMimeType = Pattern.compile("image\\/.+");

    @GetMapping
    public ApiResponse getCollections (
        @RequestParam(name = "limit", required = false, defaultValue = "50") int limit,
        @RequestParam(name = "offset", required = false, defaultValue = "0") int offset) {
        
        if (limit <= 0) limit = 10;
        if (offset < 0) offset = 0;

        return new ApiSuccessResponse<>(collectionService.getCollectionsList(limit, offset));
    }

    @PostMapping
    public ApiResponse createCollection (
        @AuthenticationPrincipal ProfileDetails profile,
        @RequestPart(name = "file") MultipartFile file,
        @ModelAttribute(name = "metadata") @Valid CreateCollectionMetadata metadata) throws IOException, ApiError {
        
        if (profile == null) return ApiResponse.getFailureResponse();
        
        if (!fileTypeService.checkMimeType(file.getInputStream(), imageMimeType)) {
            throw new UnexpectedMediaType("File must be an image");
        }

        String imageHash = ipfsService.uploadFile(file.getResource()).block();

        try {
            Collection collection = collectionRepository.insertCollection(metadata.getName(), metadata.getDescription(), profile.getId(), imageHash);
            return new ApiSuccessResponse<>(Map.of("id", collection.getId(), "imageUrl", ipfsService.resolveHashUrl(imageHash)));
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicationError("Collection with the same name already exist.", "name");
        }
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
        
        return new ApiSuccessResponse<>(this.tokenService.getTokensList(id, sortBy, limit, offset, maxPrice));
    }

    @ModelAttribute
    public void parseMetadata (@RequestPart(name = "metadata") String metadataStr, Model Model) {
        try {
            CreateCollectionMetadata metadata = objectMapper.readValue(metadataStr, CreateCollectionMetadata.class);
            Model.addAttribute("metadata", metadata);
        } catch (JacksonException ex) {
            log.error("[parseMetadata] : " + ex.getMessage());
        }
    }
}
