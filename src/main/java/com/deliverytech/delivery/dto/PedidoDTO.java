package com.deliverytech.delivery.dto;

import com.deliverytech.delivery.validation.ValidCEP;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "DTO para criação de pedido")
public class PedidoDTO {

    @NotNull(message = "ID do cliente é obrigatório")
    @Positive(message = "ID do cliente deve ser um número positivo")
    @Schema(description = "ID do cliente", example = "1", required = true)
    private Long clienteId;

    @NotNull(message = "ID do restaurante é obrigatório")
    @Positive(message = "ID do restaurante deve ser um número positivo")
    @Schema(description = "ID do restaurante", example = "1", required = true)
    private Long restauranteId;

    @NotBlank(message = "Endereço de entrega é obrigatório")
    @Size(min = 10, max = 255, message = "Endereço deve ter entre 10 e 255 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ0-9\\s,.-]+$", message = "Endereço contém caracteres inválidos")
    @Schema(description = "Endereço completo para entrega", 
            example = "Rua das Flores, 123, Apto 45 - Jardim Paulista - São Paulo/SP", required = true)
    private String enderecoEntrega;

    @NotBlank(message = "CEP de entrega é obrigatório")
    @ValidCEP(message = "CEP deve estar no formato válido brasileiro")
    @Schema(description = "CEP para entrega", example = "01234-567", required = true)
    private String cepEntrega;

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    @Schema(description = "Observações especiais do pedido", 
            example = "Sem cebola na pizza. Entrega no portão lateral.")
    private String observacoes;

    @NotEmpty(message = "Lista de itens não pode estar vazia")
    @Size(min = 1, max = 20, message = "Pedido deve ter entre 1 e 20 itens")
    @Valid
    @Schema(description = "Lista de itens do pedido", required = true)
    private List<ItemPedidoDTO> itens;

    @Pattern(regexp = "^(DINHEIRO|CARTAO_CREDITO|CARTAO_DEBITO|PIX|VALE_REFEICAO)$", 
             message = "Forma de pagamento deve ser: DINHEIRO, CARTAO_CREDITO, CARTAO_DEBITO, PIX ou VALE_REFEICAO")
    @Schema(description = "Forma de pagamento", example = "PIX",
            allowableValues = {"DINHEIRO", "CARTAO_CREDITO", "CARTAO_DEBITO", "PIX", "VALE_REFEICAO"})
    private String formaPagamento;

    @DecimalMin(value = "0.0", message = "Valor do troco deve ser maior ou igual a zero")
    @DecimalMax(value = "1000.0", message = "Valor do troco deve ser menor que R$ 1000,00")
    @Digits(integer = 4, fraction = 2, message = "Valor do troco deve ter no máximo 4 dígitos inteiros e 2 decimais")
    @Schema(description = "Valor para troco (apenas para pagamento em dinheiro)", example = "50.00")
    private BigDecimal valorTroco;

    @Future(message = "Data de entrega deve ser no futuro")
    @Schema(description = "Data e hora desejada para entrega (opcional)")
    private LocalDateTime dataEntregaDesejada;

    @Size(max = 100, message = "Nome do contato deve ter no máximo 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "Nome do contato deve conter apenas letras e espaços")
    @Schema(description = "Nome da pessoa que receberá o pedido", example = "João Silva")
    private String nomeContato;

    @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?9?\\d{4}-?\\d{4}$", 
             message = "Telefone deve estar no formato brasileiro válido")
    @Schema(description = "Telefone de contato para entrega", example = "(11) 99999-9999")
    private String telefoneContato;

    // Constructors
    public PedidoDTO() {}

    public PedidoDTO(Long clienteId, Long restauranteId, String enderecoEntrega, String cepEntrega, String observacoes, List<ItemPedidoDTO> itens) {
        this.clienteId = clienteId;
        this.restauranteId = restauranteId;
        this.enderecoEntrega = enderecoEntrega;
        this.cepEntrega = cepEntrega;
        this.observacoes = observacoes;
        this.itens = itens;
    }

    // Getters and Setters
    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public String getCepEntrega() {
        return cepEntrega;
    }

    public void setCepEntrega(String cepEntrega) {
        this.cepEntrega = cepEntrega;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoDTO> itens) {
        this.itens = itens;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public BigDecimal getValorTroco() {
        return valorTroco;
    }

    public void setValorTroco(BigDecimal valorTroco) {
        this.valorTroco = valorTroco;
    }

    public LocalDateTime getDataEntregaDesejada() {
        return dataEntregaDesejada;
    }

    public void setDataEntregaDesejada(LocalDateTime dataEntregaDesejada) {
        this.dataEntregaDesejada = dataEntregaDesejada;
    }

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getTelefoneContato() {
        return telefoneContato;
    }

    public void setTelefoneContato(String telefoneContato) {
        this.telefoneContato = telefoneContato;
    }

    // Métodos utilitários

    /**
     * Valida se o pedido está completo e válido
     */
    public boolean isPedidoValido() {
        return clienteId != null && clienteId > 0 &&
               restauranteId != null && restauranteId > 0 &&
               enderecoEntrega != null && !enderecoEntrega.trim().isEmpty() &&
               cepEntrega != null && !cepEntrega.trim().isEmpty() &&
               itens != null && !itens.isEmpty();
    }

    /**
     * Calcula o total de itens no pedido
     */
    public int getTotalItens() {
        if (itens == null) return 0;
        return itens.stream().mapToInt(item -> item.getQuantidade() != null ? item.getQuantidade() : 0).sum();
    }

    /**
     * Verifica se é necessário troco (pagamento em dinheiro)
     */
    public boolean precisaTroco() {
        return "DINHEIRO".equals(formaPagamento) && valorTroco != null && valorTroco.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Normaliza os dados do pedido
     */
    public void normalizarDados() {
        if (enderecoEntrega != null) {
            enderecoEntrega = enderecoEntrega.trim();
        }
        if (cepEntrega != null) {
            cepEntrega = cepEntrega.replaceAll("[^0-9]", "");
            if (cepEntrega.length() == 8) {
                cepEntrega = cepEntrega.substring(0, 5) + "-" + cepEntrega.substring(5);
            }
        }
        if (observacoes != null) {
            observacoes = observacoes.trim();
        }
        if (nomeContato != null) {
            nomeContato = nomeContato.trim();
        }
        if (telefoneContato != null) {
            telefoneContato = telefoneContato.replaceAll("[^0-9]", "");
        }
    }

    /**
     * Valida se a forma de pagamento é compatível com o valor do troco
     */
    public boolean isFormaPagamentoValida() {
        if ("DINHEIRO".equals(formaPagamento)) {
            return true; // Troco é opcional para dinheiro
        }
        // Para outras formas de pagamento, não deve ter troco
        return valorTroco == null || valorTroco.compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public String toString() {
        return "PedidoDTO{" +
                "clienteId=" + clienteId +
                ", restauranteId=" + restauranteId +
                ", enderecoEntrega='" + enderecoEntrega + '\'' +
                ", cepEntrega='" + cepEntrega + '\'' +
                ", observacoes='" + observacoes + '\'' +
                ", itens=" + (itens != null ? itens.size() + " itens" : "null") +
                ", formaPagamento='" + formaPagamento + '\'' +
                ", valorTroco=" + valorTroco +
                ", dataEntregaDesejada=" + dataEntregaDesejada +
                ", nomeContato='" + nomeContato + '\'' +
                ", telefoneContato='" + telefoneContato + '\'' +
                '}';
    }
}