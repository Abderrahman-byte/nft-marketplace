package com.stibits.rnft.marketplace.web;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
import com.stibits.rnft.marketplace.api.CreateCollectionMetadata;
import com.stibits.rnft.marketplace.domain.Collection;
import com.stibits.rnft.marketplace.repositories.CollectionRepository;

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
    private FileTypeService fileTypeService;

    @Autowired
    private CollectionRepository collectionRepository;

    private Pattern imageMimeType = Pattern.compile("image\\/.+");

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
