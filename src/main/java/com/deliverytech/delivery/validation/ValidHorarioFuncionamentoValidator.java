package com.deliverytech.delivery.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class ValidHorarioFuncionamentoValidator implements ConstraintValidator<ValidHorarioFuncionamento, String> {

    private static final Pattern HORARIO_PATTERN = Pattern.compile(
        "^([01]?[0-9]|2[0-3]):[0-5][0-9]-([01]?[0-9]|2[0-3]):[0-5][0-9]$"
    );
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");

    @Override
    public void initialize(ValidHorarioFuncionamento constraintAnnotation) {
        // Inicialização se necessária
    }

    @Override
    public boolean isValid(String horario, ConstraintValidatorContext context) {
        // Permite valores nulos (use @NotNull separadamente se obrigatório)
        if (horario == null || horario.trim().isEmpty()) {
            return true;
        }

        String horarioNormalizado = horario.trim();
        
        // Verifica o padrão básico
        if (!HORARIO_PATTERN.matcher(horarioNormalizado).matches()) {
            return false;
        }

        // Divide em horário de abertura e fechamento
        String[] partes = horarioNormalizado.split("-");
        if (partes.length != 2) {
            return false;
        }

        try {
            LocalTime abertura = LocalTime.parse(partes[0], TIME_FORMATTER);
            LocalTime fechamento = LocalTime.parse(partes[1], TIME_FORMATTER);
            
            // Validações de negócio
            return validarHorarios(abertura, fechamento, context);
            
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean validarHorarios(LocalTime abertura, LocalTime fechamento, ConstraintValidatorContext context) {
        // Caso 1: Funcionamento no mesmo dia (abertura < fechamento)
        if (abertura.isBefore(fechamento)) {
            return validarDuracaoMinima(abertura, fechamento, context);
        }
        
        // Caso 2: Funcionamento 24h (abertura == fechamento)
        if (abertura.equals(fechamento)) {
            // Permite apenas se for 00:00-00:00 (24h)
            return abertura.equals(LocalTime.MIDNIGHT);
        }
        
        // Caso 3: Funcionamento que cruza meia-noite (abertura > fechamento)
        // Ex: 22:00-02:00 (fecha no dia seguinte)
        return validarHorarioCruzaMeiaNoite(abertura, fechamento, context);
    }

    private boolean validarDuracaoMinima(LocalTime abertura, LocalTime fechamento, ConstraintValidatorContext context) {
        // Deve funcionar pelo menos 1 hora
        long minutos = java.time.Duration.between(abertura, fechamento).toMinutes();
        
        if (minutos < 60) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "Restaurante deve funcionar por pelo menos 1 hora"
            ).addConstraintViolation();
            return false;
        }
        
        // Não deve funcionar mais que 18 horas seguidas
        if (minutos > 18 * 60) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "Restaurante não deve funcionar mais que 18 horas seguidas"
            ).addConstraintViolation();
            return false;
        }
        
        return true;
    }

    private boolean validarHorarioCruzaMeiaNoite(LocalTime abertura, LocalTime fechamento, ConstraintValidatorContext context) {
        // Calcula duração considerando que cruza meia-noite
        // Ex: 22:00-02:00 = 4 horas (22:00-00:00 + 00:00-02:00)
        long minutosAntesDeZero = java.time.Duration.between(abertura, LocalTime.MIDNIGHT).toMinutes();
        long minutosDepoisDeZero = java.time.Duration.between(LocalTime.MIDNIGHT, fechamento).toMinutes();
        long totalMinutos = minutosAntesDeZero + minutosDepoisDeZero;
        
        if (totalMinutos < 60) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "Restaurante deve funcionar por pelo menos 1 hora"
            ).addConstraintViolation();
            return false;
        }
        
        if (totalMinutos > 18 * 60) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "Restaurante não deve funcionar mais que 18 horas seguidas"
            ).addConstraintViolation();
            return false;
        }
        
        return true;
    }
}