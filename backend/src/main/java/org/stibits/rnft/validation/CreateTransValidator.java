package org.stibits.rnft.validation;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class CreateTransValidator extends GenericMapValidator {

	public CreateTransValidator() {
		this.addRequiredFields("id");
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void validate(Object target, Errors errors) {
		Map<String, Object> data = (Map<String,Object> ) target;
		
		this.checkRequiredFields(data, errors);
		this.checkAllowedFields(data, errors);

		if (errors.hasErrors()) return ;
	        
		this.checkStringValues(data, this.getRequiredFields(), errors);
	}
}
