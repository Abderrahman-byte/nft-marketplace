package com.stibits.rnft.marketplace.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
import com.stibits.rnft.common.utils.RandomStringGenerator;
import com.stibits.rnft.marketplace.api.AssetContent;
import com.stibits.rnft.marketplace.api.CreateTokenMetadata;
import com.stibits.rnft.marketplace.domain.Collection;
import com.stibits.rnft.marketplace.domain.Token;
import com.stibits.rnft.marketplace.errors.CollectionNotFoundError;
import com.stibits.rnft.marketplace.repositories.CollectionRepository;
import com.stibits.rnft.marketplace.repositories.TokenRepository;
import com.stibits.rnft.marketplace.services.TokenService;

@RestController
@RequestMapping("/tokens")
public class CreateTokenController {
    private static final String ASSET_PREFIX = "STIBITS/RNFT/";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IpfsService ipfsService;

    @Autowired
    private FileTypeService fileTypeService;

    private Pattern tokenMimeType = Pattern.compile("image\\/.+");

    // TODO : add source url
    // TODO : check if the artist is the owner of the collection

    @PostMapping
    public ApiResponse createToken (
        @AuthenticationPrincipal ProfileDetails profileDetails,
        @RequestPart(name = "file") MultipartFile file,
        @ModelAttribute(name = "metadata") @Valid CreateTokenMetadata metadata) throws IOException, ApiError {
        
        if (profileDetails == null) return ApiResponse.getFailureResponse();

        Collection collection = null;

        if (metadata.getCollectionId() != null) {
            collection = collectionRepository.selectCollectionById(metadata.getCollectionId());

            if (collection == null) throw new CollectionNotFoundError();

            if (!collection.getCreatedById().equals(profileDetails.getId())) 
                throw new AccessDeniedException("Collection is not owner by you.");
        }

        if (tokenRepository.selectByTitle(metadata.getTitle()) != null) {
            throw new DuplicationError("Token with same title already exists.", "title");
        }

        InputStream fileInputStream = file.getInputStream();

        if (!fileTypeService.checkMimeType(fileInputStream, tokenMimeType)) {
            throw new UnexpectedMediaType("Unexpected token type, types supported are PNG, GIF, WEBP, MP4, MP3.");
        }

        String tokenHash = ipfsService.uploadFile(file.getResource()).block();
        String assetName = ASSET_PREFIX + RandomStringGenerator.generateStr(30 - ASSET_PREFIX.length()).toUpperCase();
        AssetContent content = new AssetContent();

        content.setArtist(profileDetails.getDisplayName());
        content.setTitle(metadata.getTitle());
        content.setFileIpfs(tokenHash);

        String metadataHash = ipfsService.uploadData(objectMapper.writeValueAsString(content)).block();

        if (!StringUtils.hasText(tokenHash) || !StringUtils.hasText(metadataHash)) {
            throw new UnknownError();
        }

        Token token = tokenService.createToken(metadata, collection, assetName, tokenHash, metadataHash, profileDetails.getId());

        return new ApiSuccessResponse<>(Map.of("id", token.getId(), "previewUrl", ipfsService.resolveHashUrl(tokenHash)));
    } 

    @ModelAttribute
    public void parseMetadata (@RequestPart(name = "metadata") String metadataStr, Model model) {
        try {
            CreateTokenMetadata metadata = objectMapper.readValue(metadataStr, CreateTokenMetadata.class);
            model.addAttribute("metadata", metadata);
        } catch (JacksonException ex) {}
    }
}
