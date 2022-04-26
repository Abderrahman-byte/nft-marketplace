package org.stibits.rnft.controllers.profile;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.*;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.StorageUnacceptedMediaType;
import org.stibits.rnft.errors.UnacceptedMediaTypeError;
import org.stibits.rnft.helpers.StorageService;
import org.stibits.rnft.model.bo.Account;
import org.stibits.rnft.model.bo.Profile;
import org.stibits.rnft.model.dao.AccountDAO;
import org.stibits.rnft.model.dao.ProfileDAO;

@RestController
@RequestMapping("/api/${api.version}/marketplace/profile/picture")
public class ProfilepictureController {

	@Autowired 
	private ProfileDAO profiledao;
	@Autowired
    private StorageService storageService;
	
	

	
	private Pattern fileformat = Pattern.compile("^(image|video|audio)+/(png|gif|webp|mp4|mp3|jpeg|svg)+$");
	
	@PostMapping
	public Map<String, Object> insert(@RequestParam( name = "file")  MultipartFile file,
			@RequestAttribute(name = "account", required = true) Account account,
			@RequestParam(name="Type",required = true ) String Type,
			HttpServletRequest request )throws ApiError 
	{
		Map<String, Object> response = new HashMap<>();
		
		response.put("success", true);
		if(file ==null || file.isEmpty()) {
			response.put("success", false);
		}
		  try {
	            String CustomUrl = storageService.storeFile(file, fileformat);
	            String contentFullUrl = ServletUriComponentsBuilder.fromContextPath(request).replacePath("/").pathSegment("media", CustomUrl).build().toUriString();
	           System.out.println(contentFullUrl);
	          
	            Profile p = account.getProfile();
	            System.out.println("here"+p.getDisplayName());
	           
	            if(Type.equals("avatar")) {
	            
	            	p.setAvatarUrl(contentFullUrl);
	            
	            } else 
	            {
	               p.setCoverUrl(contentFullUrl);
	            }
	            profiledao.insertProfile(account, p);
	           return response;
	        } catch (StorageUnacceptedMediaType ex) {
	            throw new UnacceptedMediaTypeError(ex);
	        } 
		
	}
	
	
	
}
