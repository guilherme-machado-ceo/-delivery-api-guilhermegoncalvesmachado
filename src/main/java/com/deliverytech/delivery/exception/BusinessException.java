package com.deliverytech.delivery.exception;

/**
 * Exceção base para regras de negócio violadas
 * Deve ser usada quando uma operação não pode ser realizada devido a regras específicas do domínio
 */
public class BusinessException extends RuntimeException {
    
    private final String errorCode;
    private final Object[] parameters;

    public BusinessException(String message) {
        super(message);
        this.errorCode = "BUSINESS_RULE_VIOLATION";
        this.parameters = new Object[0];
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "BUSINESS_RULE_VIOLATION";
        this.parameters = new Object[0];
    }

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.parameters = new Object[0];
    }

    public BusinessException(String errorCode, String message, Object... parameters) {
        super(message);
        this.errorCode = errorCode;
        this.parameters = parameters != null ? parameters : new Object[0];
    }

    public BusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.parameters = new Object[0];
    }

    public BusinessException(String errorCode, String message, Throwable cause, Object... parameters) {
        super(message, cause);
        this.errorCode = errorCode;
        this.parameters = parameters != null ? parameters : new Object[0];
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Object[] getParameters() {
        return parameters;
    }

    /**
     * Cria uma BusinessException para restaurante inativo
     */
    public static BusinessException restauranteInativo(Long restauranteId) {
        return new BusinessException(
            "RESTAURANTE_INATIVO",
            "Restaurante não está disponível para pedidos",
            restauranteId
        );
    }

    /**
     * Cria uma BusinessException para produto indisponível
     */
    public static BusinessException produtoIndisponivel(Long produtoId) {
        return new BusinessException(
            "PRODUTO_INDISPONIVEL",
            "Produto não está disponível no momento",
            produtoId
        );
    }

    /**
     * Cria uma BusinessException para pedido que não pode ser cancelado
     */
    public static BusinessException pedidoNaoPodeCancelar(Long pedidoId, String status) {
        return new BusinessException(
            "PEDIDO_NAO_PODE_CANCELAR",
            "Pedido não pode ser cancelado no status atual: " + status,
            pedidoId, status
        );
    }

    /**
     * Cria uma BusinessException para horário de funcionamento
     */
    public static BusinessException restauranteFechado(Long restauranteId, String horario) {
        return new BusinessException(
            "RESTAURANTE_FECHADO",
            "Restaurante está fechado no horário: " + horario,
            restauranteId, horario
        );
    }

    /**
     * Cria uma BusinessException para valor mínimo de pedido
     */
    public static BusinessException valorMinimoNaoAtingido(java.math.BigDecimal valorMinimo, java.math.BigDecimal valorPedido) {
        return new BusinessException(
            "VALOR_MINIMO_NAO_ATINGIDO",
            String.format("Valor mínimo do pedido é R$ %.2f, valor atual: R$ %.2f", valorMinimo, valorPedido),
            valorMinimo, valorPedido
        );
    }
}