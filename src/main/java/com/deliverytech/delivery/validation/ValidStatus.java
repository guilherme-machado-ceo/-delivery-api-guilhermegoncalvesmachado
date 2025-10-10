package com.deliverytech.delivery.validation;

import com.deliverytech.delivery.model.StatusPedido;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidStatusValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStatus {
    String message() default "Transição de status não permitida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    /**
     * Status atual do pedido para validar a transição
     */
    StatusPedido currentStatus() default StatusPedido.PENDENTE;
}