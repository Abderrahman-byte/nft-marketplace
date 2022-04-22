package org.stibits.rnft.validation;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;

public abstract class GenericMapValidator extends GenericValidator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Map.class) || Map.class.isAssignableFrom(clazz);
    }

    protected void checkAllowedFields(Map<String, Object> data, Errors errors) {
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

    protected void checkRequiredFields(Map<String, Object> data, Errors errors) {
        List<String> requiredFields = this.getRequiredFields();

        for (String field : requiredFields) {
            if (!data.containsKey(field) || data.get(field) == null || data.get(field).equals("")) {
                String defaultMessage = "The field " + field + " is required";
                errors.rejectValue(field, "requiredField", new Object[] { field }, defaultMessage);
            }
        }
    }

    protected void checkStringValues(Map<String, Object> data, List<String> stringFields, Errors errors) {
        this.checkFieldsType(data, stringFields, String.class, errors);
    }

    protected void checkBooleanValues (Map<String, Object> data, List<String> booleanFields, Errors errors) {
        this.checkFieldsType(data, booleanFields, Boolean.class, errors);
    }

    protected void testRegex (Pattern pattern, String field, Object value, Errors errors) {
        this.testRegex(pattern, field, (String)value, errors);
    }

    protected void testRegex (Pattern pattern, String field, String value, Errors errors) {
        Matcher matcher = pattern.matcher(value);

        if (matcher.find()) return ;
        
        if (matcher.matches()) return ;

        errors.rejectValue(field, "invalidValue");
    }

    protected void checkPositiveIntegers (Map<String, Object> data, List<String> fields, Errors errors) {
        for (String field : fields) {
            if (!data.containsKey(field)) return;

            if (data.get(field) == null || !data.get(field).getClass().equals(Integer.class)) {
                errors.rejectValue(field, "invalidType");
                return ;
            }

            int value = (Integer)data.get(field);

            if (value <= 0) errors.rejectValue(field, "invalidValue");
        }
    }

    private void checkFieldsType(Map<String, Object> data, List<String> fields, Class<?> typeClass, Errors errors) {
        for (String field : fields) {
            if (!data.containsKey(field)) return;

            if (data.get(field) == null || !data.get(field).getClass().equals(typeClass))
                errors.rejectValue(field, "invalidType");
        }
    }
}
