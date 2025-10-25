package com.deliverytech.delivery.exception;

/**
 * Exceção para conflitos de dados (duplicação, violação de constraints)
 * Resulta em HTTP 409 Conflict
 */
public class ConflictException extends RuntimeException {
    
    private final String conflictType;
    private final Object conflictValue;
    private final String errorCode;

    public ConflictException(String message) {
        super(message);
        this.conflictType = "GENERIC_CONFLICT";
        this.conflictValue = null;
        this.errorCode = "CONFLICT";
    }

    public ConflictException(String conflictType, Object conflictValue, String message) {
        super(message);
        this.conflictType = conflictType;
        this.conflictValue = conflictValue;
        this.errorCode = "CONFLICT";
    }

    public ConflictException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.conflictType = "GENERIC_CONFLICT";
        this.conflictValue = null;
        this.errorCode = errorCode;
    }

    public ConflictException(String conflictType, Object conflictValue, String message, Throwable cause) {
        super(message, cause);
        this.conflictType = conflictType;
        this.conflictValue = conflictValue;
        this.errorCode = "CONFLICT";
    }

    public String getConflictType() {
        return conflictType;
    }

    public Object getConflictValue() {
        return conflictValue;
    }

    public String getErrorCode() {
        return errorCode;
    }

    // Factory methods para conflitos específicos

    public static ConflictException emailJaExiste(String email) {
        return new ConflictException("EMAIL_DUPLICADO", email, 
            "Já existe um cliente cadastrado com este email: " + email);
    }

    public static ConflictException telefoneJaExiste(String telefone) {
        return new ConflictException("TELEFONE_DUPLICADO", telefone, 
            "Já existe um cliente cadastrado com este telefone: " + telefone);
    }

    public static ConflictException restauranteJaExiste(String nome) {
        return new ConflictException("RESTAURANTE_DUPLICADO", nome, 
            "Já existe um restaurante cadastrado com este nome: " + nome);
    }

    public static ConflictException produtoJaExiste(String nome, Long restauranteId) {
        return new ConflictException("PRODUTO_DUPLICADO", nome, 
            String.format("Já existe um produto com nome '%s' neste restaurante", nome));
    }

    public static ConflictException usuarioJaExiste(String username) {
        return new ConflictException("USUARIO_DUPLICADO", username, 
            "Já existe um usuário cadastrado com este nome: " + username);
    }

    public static ConflictException cpfJaExiste(String cpf) {
        return new ConflictException("CPF_DUPLICADO", cpf, 
            "Já existe um cliente cadastrado com este CPF: " + cpf);
    }

    public static ConflictException cnpjJaExiste(String cnpj) {
        return new ConflictException("CNPJ_DUPLICADO", cnpj, 
            "Já existe um restaurante cadastrado com este CNPJ: " + cnpj);
    }

    public static ConflictException pedidoJaProcessado(Long pedidoId) {
        return new ConflictException("PEDIDO_JA_PROCESSADO", pedidoId, 
            "Pedido já foi processado e não pode ser alterado");
    }

    public static ConflictException statusInvalido(String statusAtual, String novoStatus) {
        return new ConflictException("STATUS_INVALIDO", novoStatus, 
            String.format("Não é possível alterar status de '%s' para '%s'", statusAtual, novoStatus));
    }

    public static ConflictException recursoEmUso(String recurso, Object id) {
        return new ConflictException("RECURSO_EM_USO", id, 
            String.format("%s está sendo usado e não pode ser removido", recurso));
    }

    public static ConflictException limiteExcedido(String recurso, int limite) {
        return new ConflictException("LIMITE_EXCEDIDO", limite, 
            String.format("Limite de %s excedido: %d", recurso, limite));
    }
}