package org.stibits.rnft.validation;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class LoginFormValidator extends GenericMapValidator {
	public LoginFormValidator() {
		this.addRequiredFields("username", "password");	
	}
  
	@Override
	@SuppressWarnings("unchecked")
	public void validate(Object target, Errors errors) {
		Map<String, Object> data = (Map<String, Object>)target;
		
		this.checkAllowedFields(data, errors);
	    this.checkRequiredFields(data, errors);
	    
	    if (errors.hasErrors()) return ;
	    
	    this.checkStringValues(data, List.of("username", "password"), errors);
	}

}