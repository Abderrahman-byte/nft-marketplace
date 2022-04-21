package org.stibits.rnft.validation;

import org.springframework.validation.Errors;

public class CreateSingleNFTValidator extends GenericMapValidator {
    public CreateSingleNFTValidator () {
        this.addRequiredFields("title");
    }

    @Override
    public void validate(Object target, Errors errors) {
        
    }
}
