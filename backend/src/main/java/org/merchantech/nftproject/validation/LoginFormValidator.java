package org.merchantech.nftproject.validation;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;

public class LoginFormValidator extends GenericMapValidator {
    List<String> stringFields = List.of("username", "password");
    
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
	    
	    this.checkStringValues(data, stringFields, errors);
		
	    if(errors.hasErrors()) return;
	   
	}

}
