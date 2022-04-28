package org.stibits.rnft.controllers.profile;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.Profile;
import org.stibits.rnft.repositories.ProfileDAO;

@RestController
@RequestMapping("/api/${api.version}/profile/picture")
public class ProfilePictureController {
	@Autowired
	private ProfileDAO profiledao;
	@Autowired

    private StorageService storageService;
	
	private Pattern fileformat = Pattern.compile("^(image|video|audio)+/(png|gif|webp|mp4|mp3|jpeg|svg)+$");
	
	@PostMapping
	public Map<String, Object> insert(@RequestParam(name = "file") MultipartFile file,
			@RequestAttribute(name = "account", required = true) Account account,
			@RequestParam(name = "Type", required = true) String Type,
			HttpServletRequest request) throws ApiError {
		Map<String, Object> response = new HashMap<>();

		if (file == null || file.isEmpty()) {
			response.put("success", false);
			return response;
		}

		response.put("success", true);
		
		try {
			Profile p = account.getProfile();
			String CustomUrl = storageService.storeFile(file, fileformat);
			String contentFullUrl = ServletUriComponentsBuilder
					.fromContextPath(request).replacePath("/")
					.pathSegment("media", CustomUrl)
					.build().toUriString();

			if (Type.equals("cover")) p.setCoverUrl(contentFullUrl);
			else p.setAvatarUrl(contentFullUrl);

			profiledao.insertProfile(account, p);

			return response;
		} catch (StorageUnacceptedMediaType ex) {
			throw new UnacceptedMediaTypeError(ex);
		}
	}
}
