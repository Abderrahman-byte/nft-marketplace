package org.stibits.rnft.validation;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class CreateBidValidator extends GenericMapValidator {
    public CreateBidValidator () {
        this.addRequiredFields("tokenId", "to", "price");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void validate(Object target, Errors errors) {
        Map<String, Object> data = (Map<String, Object>)target;

        this.checkRequiredFields(data, errors);

        if (errors.hasErrors()) return ;
        
        this.checkAllowedFields(data, errors);
        this.checkRequiredFields(data, errors);

        if (errors.hasErrors()) return ;

        this.checkStringValues(data, List.of("tokenId", "to"), errors);

        if (errors.hasErrors()) return ;

        double price = 0;

        if (data.get("price").getClass().equals(Integer.class)) {
            price = Double.valueOf((Integer)data.get("price"));
        } else if (data.get("price").getClass().equals(Double.class)) {
            price = (Double)data.get("price");
        } else {
            errors.rejectValue("price", "invalidType");
        }
        
        if (!errors.hasErrors() && price <= 0) 
            errors.rejectValue("price", "invalidValue");

        data.put("price", price);
    }
}
