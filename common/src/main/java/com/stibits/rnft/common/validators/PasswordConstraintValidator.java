package com.stibits.rnft.common.validators;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.stibits.rnft.common.anotations.StrongPassword;

import org.passay.DigitCharacterRule;
import org.passay.LengthRule;
import org.passay.LowercaseCharacterRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.UppercaseCharacterRule;

public class PasswordConstraintValidator implements ConstraintValidator<StrongPassword, String> {
    @Override
    public void initialize(StrongPassword constraintAnnotation) {
    }    

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(List.of(
            new LengthRule(8),
            new UppercaseCharacterRule(1),
            new DigitCharacterRule(1),
            new LowercaseCharacterRule(1)
        ));

        RuleResult result = validator.validate(new PasswordData(value));

        if (result.isValid()) return true;

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
            String.join(",", validator.getMessages(result))
        ).addConstraintViolation();

        return false;
    }
}
