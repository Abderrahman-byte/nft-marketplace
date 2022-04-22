package org.stibits.rnft.controllers.marketplace;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.stibits.rnft.errors.StorageUnacceptedMediaType;
import org.stibits.rnft.errors.UnacceptedMediaTypeError;
import org.stibits.rnft.helpers.StorageService;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.DataIntegrityError;
import org.stibits.rnft.errors.ValidationError;
import org.stibits.rnft.model.bo.Account;
import org.stibits.rnft.model.bo.NFToken;
import org.stibits.rnft.model.dao.NFTokenDAO;
import org.stibits.rnft.validation.CreateSingleNFTValidator;
import org.springframework.beans.factory.annotation.Autowired;
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
    private CreateSingleNFTValidator validator;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private NFTokenDAO nftokenDAO;

    private Pattern acceptedFiles = Pattern.compile("^(image|video|audio)+/(png|gif|webp|mp4|mp3|jpeg)+$");

    @PostMapping
    public Map<String, Object> handlePostRequest(
            @RequestParam(name = "multi", required = false, defaultValue = "false") boolean multi,
            @RequestParam(name = "file") MultipartFile file,
            @ModelAttribute("metadata") Map<String, Object> metadata,
            HttpServletRequest request,
            @RequestAttribute("account") Account account) throws ApiError {
        Map<String, Object> response = new HashMap<>();

        if (!multi) return this.createSingleNFT(file, metadata, request, account);

        return response;
    }

    public Map<String, Object> createSingleNFT(@RequestParam(name = "file") MultipartFile file, @ModelAttribute("metadata") Map<String, Object> metadata, HttpServletRequest request, @RequestAttribute("account") Account account) throws ApiError {
        if (metadata == null) throw new ValidationError();

        MapBindingResult errors = new MapBindingResult(metadata, "nft");

        validator.validate(metadata, errors);

        if (errors.hasErrors()) throw new ValidationError(errors, messageSource);

        if (file == null || file.isEmpty()) {
            errors.rejectValue("file", "requiredField", new Object[]{"file"}, null);
            throw new ValidationError(errors, messageSource);
        }

        try {
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> nftData = new HashMap<>();
            String nftContentUrl = storageService.storeFile(file, acceptedFiles, "nft/");
            String contentFullUrl = ServletUriComponentsBuilder.fromContextPath(request).replacePath("/").pathSegment("media", nftContentUrl).build().toUriString();

            NFToken nft = nftokenDAO.insertNFT(account, metadata, contentFullUrl);
            nftData.put("id", nft.getId());
            nftData.put("contentUrl", contentFullUrl);
            response.put("success", true);
            response.put("data", nftData);

            return response;
        } catch (StorageUnacceptedMediaType ex) {
            throw new UnacceptedMediaTypeError(ex);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityError("Item with the same title already exists", "title");
        }
    }

    @ModelAttribute
    public void parseDataPart(Model model, @RequestParam(name = "metadata") String dataStr) {
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
}
