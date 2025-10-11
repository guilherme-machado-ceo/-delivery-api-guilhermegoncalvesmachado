package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.TaxaEntregaResponse;
import com.deliverytech.delivery.exception.EntityNotFoundException;
import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.repository.RestauranteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
public class TaxaEntregaService {

    private static final Logger logger = LoggerFactory.getLogger(TaxaEntregaService.class);
    private static final Pattern CEP_PATTERN = Pattern.compile("^\\d{5}-?\\d{3}$");
    
    // Configurações de taxa
    private static final BigDecimal TAXA_MINIMA = new BigDecimal("2.00");
    private static final BigDecimal TAXA_MAXIMA = new BigDecimal("25.00");
    private static final double DISTANCIA_MAXIMA = 30.0; // km
    
    @Autowired
    private RestauranteRepository restauranteRepository;

    /**
     * Calcula taxa de entrega completa para um restaurante e CEP
     */
    public TaxaEntregaResponse calcularTaxa(Long restauranteId, String cep) {
        logger.info("Calculando taxa de entrega - Restaurante: {}, CEP: {}", restauranteId, cep);

        // Validar restaurante
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", restauranteId));

        if (!restaurante.isAtivo()) {
            return TaxaEntregaResponse.criarIndisponivel(cep, restauranteId,
                    "Restaurante temporariamente indisponível");
        }

        // Validar CEP
        if (!isValidCEP(cep)) {
            return TaxaEntregaResponse.criarIndisponivel(cep, restauranteId,
                    "CEP inválido ou não informado");
        }

        // Calcular distância e tempo
        CalculoDistancia calculo = calcularDistanciaETempo(cep, restaurante);

        // Verificar se atende a região
        if (calculo.distancia > DISTANCIA_MAXIMA) {
            return TaxaEntregaResponse.criarIndisponivel(cep, restauranteId,
                    String.format("Região muito distante (%.1f km). Máximo: %.1f km", 
                                 calculo.distancia, DISTANCIA_MAXIMA));
        }

        // Calcular taxas
        BigDecimal taxaBase = restaurante.getTaxaEntrega();
        BigDecimal taxaDistancia = calcularTaxaDistancia(calculo.distancia);
        BigDecimal taxaHorario = calcularTaxaHorario();
        BigDecimal taxaClima = calcularTaxaClima(cep);
        
        BigDecimal taxaTotal = taxaBase
                .add(taxaDistancia)
                .add(taxaHorario)
                .add(taxaClima);

        // Aplicar limites
        if (taxaTotal.compareTo(TAXA_MINIMA) < 0) {
            taxaTotal = TAXA_MINIMA;
        }
        if (taxaTotal.compareTo(TAXA_MAXIMA) > 0) {
            taxaTotal = TAXA_MAXIMA;
        }

        // Criar resposta
        TaxaEntregaResponse response = new TaxaEntregaResponse(
                taxaTotal, calculo.distancia, calculo.tempoEstimado);

        response.setCepDestino(formatarCEP(cep));
        response.setRestauranteId(restauranteId);
        response.setRestauranteNome(restaurante.getNome());
        response.setTaxaBase(taxaBase);
        response.setTaxaDistancia(taxaDistancia.add(taxaHorario).add(taxaClima));
        response.setCalculadoEm(LocalDateTime.now());
        response.setEntregaDisponivel(true);

        // Adicionar observações
        StringBuilder obs = new StringBuilder();
        if (taxaHorario.compareTo(BigDecimal.ZERO) > 0) {
            obs.append("Taxa de horário de pico aplicada. ");
        }
        if (taxaClima.compareTo(BigDecimal.ZERO) > 0) {
            obs.append("Taxa adicional por condições climáticas. ");
        }
        if (calculo.distancia > 15.0) {
            obs.append("Entrega de longa distância. ");
        }
        
        response.setObservacoes(obs.toString().trim());

        logger.info("Taxa calculada: {} para distância de {}km em {}",
                   response.getTaxaEntregaFormatada(), 
                   response.getDistanciaFormatada(),
                   response.getTempoEstimado());

        return response;
    }

    /**
     * Calcula múltiplas taxas para comparação
     */
    public Map<Long, TaxaEntregaResponse> calcularTaxasMultiplas(String cep, Long... restauranteIds) {
        logger.info("Calculando taxas múltiplas para CEP: {} e {} restaurantes", cep, restauranteIds.length);

        Map<Long, TaxaEntregaResponse> resultados = new HashMap<>();

        for (Long restauranteId : restauranteIds) {
            try {
                TaxaEntregaResponse taxa = calcularTaxa(restauranteId, cep);
                resultados.put(restauranteId, taxa);
            } catch (Exception e) {
                logger.warn("Erro ao calcular taxa para restaurante {}: {}", restauranteId, e.getMessage());
                resultados.put(restauranteId, 
                    TaxaEntregaResponse.criarIndisponivel(cep, restauranteId, 
                        "Erro no cálculo: " + e.getMessage()));
            }
        }

        return resultados;
    }

    /**
     * Simula consulta de distância e tempo baseada no CEP
     */
    private CalculoDistancia calcularDistanciaETempo(String cep, Restaurante restaurante) {
        String cepLimpo = cep.replaceAll("[^0-9]", "");
        
        // Simular diferentes regiões baseadas no CEP
        String prefixo = cepLimpo.substring(0, 2);
        String sufixo = cepLimpo.substring(5, 8);
        
        // Calcular distância base por região
        double distanciaBase = switch (prefixo) {
            case "01", "02", "03", "04", "05" -> 2.0 + Math.random() * 3.0; // Centro SP
            case "06", "07", "08", "09" -> 5.0 + Math.random() * 5.0; // Zona Leste/Oeste SP
            case "10", "11", "12", "13" -> 8.0 + Math.random() * 7.0; // Grande SP
            case "14", "15", "16", "17" -> 12.0 + Math.random() * 8.0; // Interior SP
            case "20", "21", "22", "23" -> 15.0 + Math.random() * 10.0; // RJ
            case "30", "31", "32" -> 18.0 + Math.random() * 7.0; // BH
            case "40", "41", "42" -> 20.0 + Math.random() * 8.0; // Salvador
            case "50", "51", "52" -> 22.0 + Math.random() * 6.0; // Recife
            case "60", "61" -> 25.0 + Math.random() * 5.0; // Fortaleza
            case "70", "71", "72" -> 28.0 + Math.random() * 4.0; // Brasília
            case "80", "81", "82" -> 24.0 + Math.random() * 6.0; // Curitiba
            case "90", "91", "92" -> 26.0 + Math.random() * 7.0; // Porto Alegre
            default -> 15.0 + Math.random() * 10.0; // Outras regiões
        };
        
        // Ajustar pela densidade do bairro (baseado no sufixo)
        int densidade = Integer.parseInt(sufixo) % 100;
        if (densidade < 20) {
            distanciaBase *= 0.8; // Região mais densa, menor distância
        } else if (densidade > 80) {
            distanciaBase *= 1.3; // Região menos densa, maior distância
        }
        
        // Arredondar para 1 casa decimal
        double distanciaFinal = Math.round(distanciaBase * 10.0) / 10.0;
        
        // Calcular tempo estimado
        String tempoEstimado = calcularTempoEstimado(distanciaFinal);
        
        return new CalculoDistancia(distanciaFinal, tempoEstimado);
    }

    /**
     * Calcula tempo estimado baseado na distância
     */
    private String calcularTempoEstimado(double distancia) {
        // Velocidade média considerando trânsito urbano: 20-25 km/h
        double velocidadeMedia = 22.0;
        double tempoHoras = distancia / velocidadeMedia;
        int tempoMinutos = (int) Math.ceil(tempoHoras * 60);
        
        // Adicionar tempo de preparo (15-45 min dependendo da distância)
        int tempoPreparo = (int) (15 + (distancia * 2));
        int tempoTotal = tempoMinutos + tempoPreparo;
        
        // Criar faixa de tempo
        int tempoMin = Math.max(tempoTotal - 10, 15);
        int tempoMax = tempoTotal + 15;
        
        return String.format("%d-%d min", tempoMin, tempoMax);
    }

    /**
     * Calcula taxa adicional baseada na distância
     */
    private BigDecimal calcularTaxaDistancia(double distancia) {
        if (distancia <= 3.0) {
            return BigDecimal.ZERO;
        } else if (distancia <= 7.0) {
            return new BigDecimal("1.50");
        } else if (distancia <= 12.0) {
            return new BigDecimal("3.00");
        } else if (distancia <= 20.0) {
            return new BigDecimal("5.00");
        } else {
            return new BigDecimal("8.00");
        }
    }

    /**
     * Calcula taxa adicional por horário de pico
     */
    private BigDecimal calcularTaxaHorario() {
        LocalDateTime agora = LocalDateTime.now();
        int hora = agora.getHour();
        int diaSemana = agora.getDayOfWeek().getValue();
        
        // Horários de pico: 11:30-14:00 e 18:00-21:00 em dias úteis
        boolean isHorarioPico = (diaSemana <= 5) && 
                               ((hora >= 11 && hora <= 14) || (hora >= 18 && hora <= 21));
        
        // Fins de semana: 12:00-15:00 e 19:00-22:00
        boolean isFimDeSemana = (diaSemana >= 6) && 
                               ((hora >= 12 && hora <= 15) || (hora >= 19 && hora <= 22));
        
        if (isHorarioPico || isFimDeSemana) {
            return new BigDecimal("2.00");
        }
        
        return BigDecimal.ZERO;
    }

    /**
     * Simula taxa adicional por condições climáticas
     */
    private BigDecimal calcularTaxaClima(String cep) {
        // Simulação simples baseada no CEP
        String cepLimpo = cep.replaceAll("[^0-9]", "");
        int ultimoDigito = Integer.parseInt(cepLimpo.substring(7, 8));
        
        // 20% de chance de condições adversas
        if (ultimoDigito <= 1) {
            return new BigDecimal("1.50"); // Chuva/tempo ruim
        }
        
        return BigDecimal.ZERO;
    }

    /**
     * Valida formato de CEP brasileiro
     */
    private boolean isValidCEP(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            return false;
        }

        String cepLimpo = cep.replaceAll("[^0-9]", "");
        
        // Deve ter exatamente 8 dígitos
        if (cepLimpo.length() != 8) {
            return false;
        }

        // Não pode ser sequencial
        return !cepLimpo.matches("(\\d)\\1{7}");
    }

    /**
     * Formata CEP no padrão brasileiro
     */
    private String formatarCEP(String cep) {
        if (cep == null) return null;
        
        String cepLimpo = cep.replaceAll("[^0-9]", "");
        if (cepLimpo.length() == 8) {
            return cepLimpo.substring(0, 5) + "-" + cepLimpo.substring(5);
        }
        
        return cep;
    }

    /**
     * Classe interna para resultado de cálculo de distância
     */
    private static class CalculoDistancia {
        final double distancia;
        final String tempoEstimado;

        CalculoDistancia(double distancia, String tempoEstimado) {
            this.distancia = distancia;
            this.tempoEstimado = tempoEstimado;
        }
    }
}