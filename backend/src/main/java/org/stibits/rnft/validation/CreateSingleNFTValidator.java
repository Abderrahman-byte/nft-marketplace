package org.stibits.rnft.validation;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component("singleNftValidator")
public class CreateSingleNFTValidator extends GenericMapValidator {
    public CreateSingleNFTValidator () {
        this.addRequiredFields("title", "isForSale", "instantSale");
        this.addAllowedFields("description", "price", "collectionId");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void validate(Object target, Errors errors) {
        Map<String, Object> data = (Map<String, Object>)target;

        this.checkRequiredFields(data, errors);
        this.checkAllowedFields(data, errors);

        if (errors.hasErrors()) return ;

        this.checkStringValues(data, List.of("title", "description", "collectionId"), errors);
        this.checkBooleanValues(data, List.of("isForSale", "instantSale"), errors);

        if (errors.hasErrors()) return ;

        Boolean instantSale = (Boolean)data.get("instantSale");

        // Check if the price is valid
        Boolean isValidPrice = data.containsKey("price") && (data.get("price").getClass().equals(Double.class) || data.get("price").getClass().equals(Integer.class));
        double price = 0;

        // Convert price from int to double
        if (isValidPrice)
            price = data.get("price").getClass().equals(Integer.class) ? Double.valueOf((Integer)data.get("price")) : (Double)data.get("price");

        // If instant sale is set the price also must be set
        if (instantSale && (!isValidPrice || price <= 0)) 
            errors.rejectValue("price", "invalidValue");

        data.put("price", price);
    }
}
