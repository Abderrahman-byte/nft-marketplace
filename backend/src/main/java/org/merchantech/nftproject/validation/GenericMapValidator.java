package org.merchantech.nftproject.validation;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.validation.Errors;

public abstract class GenericMapValidator extends GenericValidator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Map.class) || Map.class.isAssignableFrom(clazz);
    }

    protected void checkAllowedFields (Map<String, Object> data, Errors errors) {
        List<String> allowedFields = this.getAllowedFields();
        List<String> requiredFields = this.getRequiredFields();
        Set<String> dataKeys = data.keySet();
        
        for (String key : dataKeys) {
            if (!allowedFields.contains(key) && !requiredFields.contains(key)) {
                String defaultMessage = "The field " + key + " is not allowed";
                errors.rejectValue(key, "notAllowedField", new Object[] { key }, defaultMessage);
            }
        }
    }

    protected void checkRequiredFields (Map<String, Object> data, Errors errors) {
        List<String> requiredFields = this.getRequiredFields();

        for (String field : requiredFields) {
            if (!data.containsKey(field) || data.get(field) == null || data.get(field).equals("")) {
                String defaultMessage = "The field " + field + " is required";
                errors.rejectValue(field, "requiredField", new Object[] { field }, defaultMessage);
            }
        }
    }
}
