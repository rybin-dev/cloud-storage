package com.rybindev.cloudstorage.validator;

import com.rybindev.cloudstorage.util.PathUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PathConstraintValidator implements ConstraintValidator<Path,String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return PathUtils.isValid(value);
    }
}
