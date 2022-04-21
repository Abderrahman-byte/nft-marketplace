package org.merchantech.nftproject.validation;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ProfileFormValidator extends GenericMapValidator{
	 List<String> stringFields = List.of("display_name", "bio", "custom_url", "avatar_url");
	 
	 public ProfileFormValidator () {
	        // ! isArtist field may be move to update profile form
	        this.addRequiredFields("display_name");
	        this.addAllowedFields("display_name", "bio", "custom_url");
	    }

	@Override
	@SuppressWarnings("unchecked")
	public void validate(Object target, Errors errors) {
		Map<String, Object> data = (Map<String, Object>)target;
		
		this.checkAllowedFields(data, errors);
		this.checkStringValues(data, stringFields, errors);
		if(errors.hasErrors()) return;
		 this.checkRequiredFields(data, errors);
		 if (errors.hasErrors()) return;
		
	}
}
