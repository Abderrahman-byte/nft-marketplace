package org.stibits.rnft.validation;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class CreateTransValidator extends GenericMapValidator {

	public CreateTransValidator() {
		this.addAllowedFields("tokenId", "accountFrom");
		this.addRequiredFields("tokenId", "accountFrom");
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void validate(Object target, Errors errors) {
		Map<String, Object> data = (Map<String,Object> ) target;
		this.checkRequiredFields(data, errors);
		if (errors.hasErrors()) return ;
		this.checkAllowedFields(data, errors);
		  if (errors.hasErrors()) return ;
	        
		  this.checkStringValues(data, List.of("tokenId", "accountFrom"), errors);

	        if (errors.hasErrors()) return ;
	}
	

}
