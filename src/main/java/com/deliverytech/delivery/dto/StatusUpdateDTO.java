package com.deliverytech.delivery.dto;

import com.deliverytech.delivery.model.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para atualização de status do pedido")
public class StatusUpdateDTO {
    
    @NotNull(message = "Status é obrigatório")
    @Schema(description = "Novo status do pedido", example = "CONFIRMADO")
    private StatusPedido status;
    
    public StatusUpdateDTO() {}
    
    public StatusUpdateDTO(StatusPedido status) {
        this.status = status;
    }
    
    public StatusPedido getStatus() {
        return status;
    }
    
    public void setStatus(StatusPedido status) {
        this.status = status;
    }
}