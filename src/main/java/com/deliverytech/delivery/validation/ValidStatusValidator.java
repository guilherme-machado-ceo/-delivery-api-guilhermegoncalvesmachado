package com.deliverytech.delivery.validation;

import com.deliverytech.delivery.model.StatusPedido;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidStatusValidator implements ConstraintValidator<ValidStatus, StatusPedido> {
    
    private StatusPedido currentStatus;
    
    @Override
    public void initialize(ValidStatus constraintAnnotation) {
        this.currentStatus = constraintAnnotation.currentStatus();
    }
    
    @Override
    public boolean isValid(StatusPedido newStatus, ConstraintValidatorContext context) {
        if (newStatus == null) {
            return false;
        }
        
        return isTransicaoStatusValida(currentStatus, newStatus);
    }
    
    private boolean isTransicaoStatusValida(StatusPedido statusAtual, StatusPedido novoStatus) {
        switch (statusAtual) {
            case PENDENTE:
                return novoStatus == StatusPedido.CONFIRMADO || novoStatus == StatusPedido.CANCELADO;
            case CONFIRMADO:
                return novoStatus == StatusPedido.PREPARANDO || novoStatus == StatusPedido.CANCELADO;
            case PREPARANDO:
                return novoStatus == StatusPedido.PRONTO || novoStatus == StatusPedido.CANCELADO;
            case PRONTO:
                return novoStatus == StatusPedido.SAIU_ENTREGA;
            case SAIU_ENTREGA:
                return novoStatus == StatusPedido.ENTREGUE;
            case ENTREGUE:
            case CANCELADO:
                return false; // Status finais não podem ser alterados
            default:
                return false;
        }
    }
}