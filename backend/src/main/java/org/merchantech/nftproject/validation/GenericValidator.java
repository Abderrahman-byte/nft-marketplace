package org.merchantech.nftproject.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.Validator;

public abstract class GenericValidator implements Validator {
    private List<String> requiredFields = new ArrayList<>();
    private List<String> allowedFields = new ArrayList<>();

    protected void addAllowedFields (String ...fields) {
        this.allowedFields.addAll(List.of(fields));
    }

    protected List<String> getAllowedFields () {
        return List.copyOf(this.allowedFields);
    }

    protected void addRequiredFields (String ...fields) {
        this.requiredFields.addAll(List.of(fields));
    }

    protected List<String> getRequiredFields () {
        return List.copyOf(requiredFields);
    }
}
