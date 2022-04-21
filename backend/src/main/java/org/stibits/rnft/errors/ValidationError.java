package org.stibits.rnft.errors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@JsonIncludeProperties({ "title", "status", "detail" , "timestamp", "invalidFields" })
public class ValidationError extends ApiError {
    private List<Map<String,String>> invalidFields = new ArrayList<>();
    
    public ValidationError () {}

    public ValidationError (Errors results, MessageSource messageSource) {
        super("invalid_data", "Invalid recieved data", HttpStatus.BAD_REQUEST);

        List<FieldError> fieldErrors = results.getFieldErrors();

        for (FieldError error: fieldErrors) {
            String message = messageSource.getMessage(error, Locale.US);
            Map<String, String> fieldErrorData = new HashMap<>();
            fieldErrorData.put("name", error.getField());
            fieldErrorData.put("reason", message);
            fieldErrorData.put("code", error.getCode());

            invalidFields.add(fieldErrorData);
        }
    }

    public List<Map<String, String>> getInvalidFields() {
        return List.copyOf(this.invalidFields);
    }
}
