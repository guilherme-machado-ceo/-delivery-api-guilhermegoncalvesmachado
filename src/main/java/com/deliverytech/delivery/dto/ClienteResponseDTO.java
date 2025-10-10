package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "DTO de resposta com dados seguros do cliente")
public class ClienteResponseDTO {

    @Schema(description = "ID único do cliente", example = "1")
    private Long id;

    @Schema(description = "Nome completo do cliente", example = "João Silva")
    private String nome;

    @Schema(description = "Email do cliente", example = "joao@email.com")
    private String email;

    @Schema(description = "Telefone do cliente", example = "11999999999")
    private String telefone;

    @Schema(description = "Endereço do cliente", example = "Rua das Flores, 123 - São Paulo/SP")
    private String endereco;

    @Schema(description = "Status ativo do cliente", example = "true")
    private Boolean ativo;

    @Schema(description = "Data de cadastro", example = "2025-10-08T15:30:00")
    private LocalDateTime dataCadastro;

    // Constructors
    public ClienteResponseDTO() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}