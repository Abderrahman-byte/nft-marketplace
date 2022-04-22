package org.stibits.rnft.validation;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

// ! isArtist field may be move to update profile form

@Component
public class ProfileFormValidator extends GenericMapValidator {
	List<String> stringFields = List.of("displayName", "bio", "customUrl", "avatarUrl");

	public ProfileFormValidator() {
		this.addRequiredFields("displayName");
		this.addAllowedFields("displayName", "bio", "customUrl");
	}

	@Override
	@SuppressWarnings("unchecked")
	public void validate(Object target, Errors errors) {
		Map<String, Object> data = (Map<String, Object>) target;

		this.checkAllowedFields(data, errors);
		this.checkRequiredFields(data, errors);
		
		if (errors.hasErrors()) return;
		
		this.checkStringValues(data, stringFields, errors);
	}
}
