package org.stibits.rnft.validation;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class RegisterFormValidator extends GenericMapValidator {
    List<String> stringFields = List.of("username", "email", "password", "password2");

    Pattern usernamePattern = Pattern.compile("^[A-Za-z].{4,}$");
    Pattern emailPattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-\\.][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
    Pattern passwordRegex = Pattern.compile("(?=.*[A-Z].*)(?=.*[a-z].*)(?=.*[0-9].*)(?=.{8,})");

    public RegisterFormValidator () {
        // ! isArtist field may be move to update profile form
        this.addRequiredFields("username", "email", "password", "password2", "isArtist");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void validate(Object target, Errors errors) {
        Map<String, Object> data = (Map<String, Object>)target;

        this.checkAllowedFields(data, errors);
        this.checkRequiredFields(data, errors);

        if (errors.hasErrors()) return;

        this.checkStringValues(data, stringFields, errors);
        this.checkBooleanValues(data, List.of("isArtist"), errors);

        if (errors.hasErrors()) return;

        this.testRegex(usernamePattern, "username", data.get("username"), errors);
        this.testRegex(emailPattern, "email", data.get("email"), errors);
        this.testRegex(passwordRegex, "password", data.get("password"), errors);

        if (!data.get("password").equals(data.get("password2"))) {
            errors.rejectValue("password2", "invalidValue");
        }
    }
    
}
