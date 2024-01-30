package com.rybindev.cloudstorage.validator;

import com.rybindev.cloudstorage.util.PathUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class UploadFileConstraintValidator implements ConstraintValidator<UploadFile,MultipartFile> {
    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return value.getOriginalFilename() != null && PathUtils.isValid(value.getOriginalFilename());
    }
}
