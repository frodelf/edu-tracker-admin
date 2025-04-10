package ua.kpi.edutrackeradmin.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ImagesExtensionValidator.class)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageExtension {

    String message() default "{error.file.extension.not.valid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
