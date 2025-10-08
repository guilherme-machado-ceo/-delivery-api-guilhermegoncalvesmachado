package com.deliverytech.delivery.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidPriceValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPrice {
    String message() default "Preço deve ser um valor positivo";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}