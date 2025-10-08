package com.deliverytech.delivery.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PedidoResumo {
    Long getId();
    String getClienteNome();
    String getRestauranteNome();
    LocalDateTime getDataPedido();
    String getStatus();
    BigDecimal getValorTotal();
}