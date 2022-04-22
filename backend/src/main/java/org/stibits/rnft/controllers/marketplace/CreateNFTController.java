package org.stibits.rnft.controllers.marketplace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.stibits.rnft.errors.StorageUnacceptedMediaType;
import org.stibits.rnft.errors.UnacceptedMediaTypeError;
import org.stibits.rnft.errors.UnknownError;
import org.stibits.rnft.helpers.StorageService;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.DataIntegrityError;
import org.stibits.rnft.errors.ValidationError;
import org.stibits.rnft.model.bo.Account;
import org.stibits.rnft.model.bo.NFToken;
import org.stibits.rnft.model.dao.NFTokenDAO;
import org.stibits.rnft.validation.CreateMultipleNFTValidator;
import org.stibits.rnft.validation.CreateSingleNFTValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

// TODO : Check File max size

@RestController
@RequestMapping("/api/${api.version}/marketplace/create")
public class CreateNFTController {
    @Autowired
    private StorageService storageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("singleNftValidator")
    private CreateSingleNFTValidator singleNftFormValidator;

    @Autowired
    @Qualifier("multipleNftsValidator")
    private CreateMultipleNFTValidator multipleNFTValidator;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private NFTokenDAO nftokenDAO;

    private Pattern acceptedFiles = Pattern.compile("^(image|video|audio)+/(png|gif|webp|mp4|mp3|jpeg)+$");

    @PostMapping
    public Map<String, Object> handlePostRequest(
            @RequestParam(name = "multi", required = false, defaultValue = "false") boolean multi,
            @ModelAttribute("contentUrl") String contentUrl,
            @ModelAttribute("metadata") Map<String, Object> metadata,
            HttpServletRequest request,
            @RequestAttribute("account") Account account) throws ApiError {

        return multi 
            ? this.createMultipleNFT(metadata, contentUrl, account)
            : this.createSingleNFT(metadata, contentUrl, account);
    }

    public Map<String, Object> createMultipleNFT(Map<String, Object> metadata, String contentUrl, Account account) throws ApiError {
        try {
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> nftData = new HashMap<>();

            List<NFToken> nfts = nftokenDAO.insertMultipleNFT(account, metadata, contentUrl);

            nftData.put("items", nfts.stream().map(nft -> {
                Map<String, String> item = new HashMap<>();
                item.put("id", nft.getId());
                item.put("title", nft.getTitle());
                return item;
            }).toList());
            
            nftData.put("contentUrl", contentUrl);
            response.put("success", true);
            response.put("data", nftData);

            return response;
        } catch (Exception ex) {
            System.out.println("[" + ex.getClass().getName() + "] " + ex.getMessage());
            throw new UnknownError();
        }
    }

    public Map<String, Object> createSingleNFT(Map<String, Object> metadata, String contentUrl, Account account) throws ApiError {
        try {
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> nftData = new HashMap<>();

            NFToken nft = nftokenDAO.insertNFT(account, metadata, contentUrl);
            nftData.put("id", nft.getId());
            nftData.put("contentUrl", contentUrl);
            response.put("success", true);
            response.put("data", nftData);

            return response;
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityError("Item with the same title already exists", "title");
        }
    }

    @ModelAttribute
    public void parseMetaData(Model model, @RequestParam(name = "metadata") String dataStr) {
        if (dataStr == null || dataStr.isEmpty()) {
            model.addAttribute("metadata", null);
            return;
        }

        try {
            model.addAttribute("metadata", objectMapper.readValue(dataStr, Map.class));
        } catch (JsonProcessingException ex) {
            model.addAttribute("metadata", null);
            return;
        }
    }

    @ModelAttribute
    public void validateRequest (@RequestParam(name = "file") MultipartFile file, @ModelAttribute("metadata") Map<String, Object> metadata, @RequestParam(name = "multi", required = false, defaultValue = "false") boolean multi, HttpServletRequest request, Model model) throws ApiError {
        if (metadata == null) throw new ValidationError();

        MapBindingResult errors = new MapBindingResult(metadata, "nft");

        if (!multi) singleNftFormValidator.validate(metadata, errors);
        else multipleNFTValidator.validate(metadata, errors);

        if (errors.hasErrors()) throw new ValidationError(errors, messageSource);

        if (file == null || file.isEmpty()) {
            errors.rejectValue("file", "requiredField", new Object[]{"file"}, null);
            throw new ValidationError(errors, messageSource);
        }

        try {
            String nftContentUrl = storageService.storeFile(file, acceptedFiles, "nft/");
            String contentFullUrl = ServletUriComponentsBuilder.fromContextPath(request).replacePath("/").pathSegment("media", nftContentUrl).build().toUriString();
            model.addAttribute("contentUrl", contentFullUrl);
        } catch (StorageUnacceptedMediaType ex) {
            throw new UnacceptedMediaTypeError(ex);
        } 
    }
}
