package com.rybindev.cloudstorage.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {UploadFileConstraintValidator.class})
@Target({ FIELD,PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface UploadFile {

    String message() default "Invalid filename.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
