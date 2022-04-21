package org.stibits.rnft.controllers.marketplace;

import java.util.Map;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.stibits.rnft.errors.StorageUnacceptedMediaType;
import org.stibits.rnft.errors.UnacceptedMediaTypeError;
import org.stibits.rnft.helpers.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/${api.version}/marketplace/create")
public class CreateNFTController {
    @Autowired
    private StorageService storageService;

    @Autowired
    private ObjectMapper objectMapper;

    private Pattern acceptedFiles = Pattern.compile("^(image|video|audio)+/(png|gif|webp|mp4|mp3|jpeg)+$");

    @PostMapping
    public String handlePostRequest(@RequestParam("file") MultipartFile file, @ModelAttribute("payload") Map<String, Object> payload) throws UnacceptedMediaTypeError {
        try {

            String path = storageService.storeFile(file, acceptedFiles, "ipfs/");
            String path2 = storageService.storeFile(file, acceptedFiles);
            System.out.println("Path => " + path);
            System.out.println("Path => " + path2);

            

            System.out.println("Payload => " + payload);
        } catch (StorageUnacceptedMediaType ex) {
            throw new UnacceptedMediaTypeError(ex);
        }

        return "not_implemented";
    }

    @ModelAttribute
    public void parseDataPart(Model model, @RequestParam("data") String dataStr) {
        if (dataStr == null || dataStr.isEmpty()) {
            model.addAttribute("payload", null);
            return;
        }

        try {
            model.addAttribute("payload", objectMapper.readValue(dataStr, Map.class));
        } catch (JsonProcessingException ex) {
            model.addAttribute("payload", null);
            return;
        }
    }
}
