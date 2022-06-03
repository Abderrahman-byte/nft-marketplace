package org.stibits.rnft.validation;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

// TODO : max quantity must be 21,000,000,000 after fully supporting multiple assets

@Component("multipleNftsValidator")
public class CreateMultipleNFTValidator extends CreateSingleNFTValidator {
    private int maxQuantity = 10 ;

    public CreateMultipleNFTValidator () {
        super();
        this.addRequiredFields("quantity");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void validate(Object target, Errors errors) {
        Map<String, Object> data = (Map<String, Object>)target;

        super.validate(target, errors);

        this.checkPositiveIntegers (data, List.of("quantity"), errors);

        if (errors.hasErrors()) return ;

        int quantity = (Integer)data.get("quantity");

        if (quantity > maxQuantity)  errors.rejectValue("quantity", "maxValue");
    }
}