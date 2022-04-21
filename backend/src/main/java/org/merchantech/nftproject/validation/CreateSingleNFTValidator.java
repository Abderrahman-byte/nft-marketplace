package org.merchantech.nftproject.validation;

import java.util.Map;

import org.springframework.validation.Errors;

public class CreateSingleNFTValidator extends GenericMapValidator {
    public CreateSingleNFTValidator () {
        this.addRequiredFields("title", "isForSell");
        this.addAllowedFields("description", "price");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void validate(Object target, Errors errors) {
        Map<String, Object> data = (Map<String, Object>)target;

        this.checkRequiredFields(data, errors);
        this.checkAllowedFields(data, errors);

        if (errors.hasErrors()) return ;
    }
}
