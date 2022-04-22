package org.stibits.rnft.controllers.marketplace;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.AuthenticationRequiredError;
import org.stibits.rnft.errors.DataIntegrityError;
import org.stibits.rnft.errors.StorageUnacceptedMediaType;
import org.stibits.rnft.errors.UnacceptedMediaTypeError;
import org.stibits.rnft.errors.UnknownError;
import org.stibits.rnft.errors.ValidationError;
import org.stibits.rnft.helpers.StorageService;
import org.stibits.rnft.model.bo.Account;
import org.stibits.rnft.model.bo.NftCollection;
import org.stibits.rnft.model.dao.NftCollectionDAO;
import org.stibits.rnft.validation.CreateCollectionValidator;

// TODO : Check collection image max size
// TODO : Build response automatically using a converter or an POJO

@RestController
@RequestMapping("/api/${api.version}/marketplace/collections")
public class CreateCollection {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NftCollectionDAO collectionDAO;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private StorageService storageService;

    @Autowired
    private CreateCollectionValidator validator;

    private Pattern imageTypePattern = Pattern.compile("^image+/(png|gif|webp|jpeg)+$");

    @PostMapping
    Map<String, Object> handlePostRequest (@ModelAttribute Account account, @ModelAttribute("metadata") Map<String, Object> data, @RequestParam(name = "image", required = false) MultipartFile file, HttpServletRequest request) throws ApiError {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> createdData = new HashMap<>();
        MapBindingResult errors = new MapBindingResult(data, "collection");
        String imageUrl = null;

        validator.validate(data, errors);

        if (errors.hasErrors()) throw new ValidationError(errors, messageSource);

        if (file != null && !file.isEmpty()) {
            try {
                String imagePath = storageService.storeFile(file, imageTypePattern, "collections/");
                imageUrl = ServletUriComponentsBuilder.fromRequest(request).replacePath("/").pathSegment("media", imagePath).build().toUriString();
            } catch (StorageUnacceptedMediaType ex) {
                throw new UnacceptedMediaTypeError(ex);
            }
        }

        try {
            NftCollection collection = collectionDAO.insertCollection(account, data, imageUrl);

            response.put("success", true);
            createdData.put("id", collection.getId());
            createdData.put("imageUrl", collection.getImageUrl());
            response.put("data", createdData);

            return response;
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityError("Collection with same name already exist", "name");
        } catch (Exception ex) {
            throw new UnknownError();
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
    public void checkRequestValididty (@RequestAttribute(name = "account", required = false) Account account, Model model) throws ApiError {
        if (account == null) throw new AuthenticationRequiredError();

        model.addAttribute("account", account);

        if (!model.containsAttribute("metadata") || model.getAttribute("metadata") == null) 
            throw new ValidationError();
    }
}
