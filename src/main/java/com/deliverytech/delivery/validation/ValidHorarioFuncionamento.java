package com.deliverytech.delivery.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidHorarioFuncionamentoValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidHorarioFuncionamento {
    String message() default "Horário de funcionamento deve estar no formato HH:MM-HH:MM";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}