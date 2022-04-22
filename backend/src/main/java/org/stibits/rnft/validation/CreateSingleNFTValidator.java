package org.stibits.rnft.validation;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

// TODO : refactor

@Component("singleNftValidator")
public class CreateSingleNFTValidator extends GenericMapValidator {
    public CreateSingleNFTValidator () {
        this.addRequiredFields("title", "isForSell");
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
        this.checkBooleanValues(data, List.of("isForSell"), errors);

        if (errors.hasErrors()) return ;

        Boolean isForSell = (Boolean)data.get("isForSell");
        Boolean isValidPrice = data.containsKey("price") && data.get("price").getClass().equals(Double.class);

        if (isForSell && (!isValidPrice || ((Double)data.get("price")) <= 0)) {
            errors.rejectValue("price", "invalidValue");
        }
    }
}
