package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.EstatisticasRestauranteDTO;
import com.deliverytech.delivery.dto.FiltroRestauranteDTO;
import com.deliverytech.delivery.dto.RestauranteDTO;
import com.deliverytech.delivery.dto.RestauranteResponseDTO;
import com.deliverytech.delivery.dto.TaxaEntregaResponse;

import com.deliverytech.delivery.exception.BusinessException;
import com.deliverytech.delivery.exception.EntityNotFoundException;
import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.repository.RestauranteRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestauranteService implements RestauranteServiceInterface {
    
    private static final Logger logger = LoggerFactory.getLogger(RestauranteService.class);
    
    @Autowired
    private RestauranteRepository restauranteRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private TaxaEntregaService taxaEntregaService;
    
    // Novos métodos com DTOs padronizados
    
    /**
     * Cria um novo restaurante
     */
    public RestauranteResponseDTO criar(RestauranteDTO dto) {
        logger.info("Criando restaurante: {}", dto.getNome());
        
        // Validações de negócio
        validarRestauranteDTO(dto);
        
        // Normalizar categoria
        dto.normalizarCategoria();
        dto.definirAvaliacaoPadrao();
        
        // Validar categoria
        if (!dto.isCategoriaValida()) {
            throw new BusinessException("Categoria inválida: " + dto.getCategoria());
        }
        
        Restaurante restaurante = convertToEntity(dto);
        restaurante.setAtivo(true);
        
        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);
        
        logger.info("Restaurante criado com sucesso: ID {}", restauranteSalvo.getId());
        return convertToResponseDTO(restauranteSalvo);
    }
    
    /**
     * Lista todos os restaurantes com filtros opcionais (método otimizado)
     */
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> listarTodos(String busca, String categoria, Boolean ativo) {
        logger.info("Listando restaurantes - busca: {}, categoria: {}, ativo: {}", busca, categoria, ativo);
        
        List<Restaurante> restaurantes;
        
        // Usar queries otimizadas do repository
        if (busca != null && !busca.trim().isEmpty()) {
            if (ativo != null) {
                restaurantes = restauranteRepository.findByTextoGeralAndAtivo(busca.trim(), ativo);
            } else {
                restaurantes = restauranteRepository.findByTextoGeral(busca.trim());
            }
        } else {
            restaurantes = restauranteRepository.findWithFilters(null, categoria, null, ativo);
        }
        
        return restaurantes.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca restaurantes por nome
     */
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarPorNome(String nome) {
        logger.info("Buscando restaurantes por nome: {}", nome);
        
        if (nome == null || nome.trim().isEmpty()) {
            return List.of();
        }
        
        List<Restaurante> restaurantes = restauranteRepository.findByNomeContainingIgnoreCase(nome.trim());
        
        return restaurantes.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca restaurantes por categoria (case insensitive)
     */
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarPorCategoria(String categoria) {
        logger.info("Buscando restaurantes por categoria: {}", categoria);
        
        if (categoria == null || categoria.trim().isEmpty()) {
            return List.of();
        }
        
        List<Restaurante> restaurantes = restauranteRepository.findByCategoriaIgnoreCase(categoria.trim());
        
        return restaurantes.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca restaurantes por endereço
     */
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarPorEndereco(String endereco) {
        logger.info("Buscando restaurantes por endereço: {}", endereco);
        
        if (endereco == null || endereco.trim().isEmpty()) {
            return List.of();
        }
        
        List<Restaurante> restaurantes = restauranteRepository.findByEnderecoContainingIgnoreCase(endereco.trim());
        
        return restaurantes.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca restaurantes por avaliação mínima
     */
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarPorAvaliacaoMinima(Double avaliacaoMinima, Boolean apenasAtivos) {
        logger.info("Buscando restaurantes com avaliação >= {}, apenas ativos: {}", avaliacaoMinima, apenasAtivos);
        
        if (avaliacaoMinima == null) {
            avaliacaoMinima = 0.0;
        }
        
        List<Restaurante> restaurantes;
        if (apenasAtivos != null && apenasAtivos) {
            restaurantes = restauranteRepository.findByAvaliacaoGreaterThanEqualAndAtivo(avaliacaoMinima, true);
        } else {
            restaurantes = restauranteRepository.findByAvaliacaoGreaterThanEqual(avaliacaoMinima);
        }
        
        return restaurantes.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca restaurantes por faixa de taxa de entrega
     */
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarPorFaixaTaxa(BigDecimal taxaMin, BigDecimal taxaMax, Boolean apenasAtivos) {
        logger.info("Buscando restaurantes com taxa entre {} e {}, apenas ativos: {}", taxaMin, taxaMax, apenasAtivos);
        
        if (taxaMin == null) taxaMin = BigDecimal.ZERO;
        if (taxaMax == null) taxaMax = new BigDecimal("999.99");
        
        List<Restaurante> restaurantes;
        if (apenasAtivos != null && apenasAtivos) {
            restaurantes = restauranteRepository.findByTaxaEntregaBetweenAndAtivo(taxaMin, taxaMax, true);
        } else {
            restaurantes = restauranteRepository.findByTaxaEntregaBetween(taxaMin, taxaMax);
        }
        
        return restaurantes.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Lista restaurantes ordenados por critério
     */
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> listarOrdenados(String ordenacao, Boolean apenasAtivos) {
        logger.info("Listando restaurantes ordenados por: {}, apenas ativos: {}", ordenacao, apenasAtivos);
        
        Boolean filtroAtivo = apenasAtivos != null ? apenasAtivos : true;
        List<Restaurante> restaurantes;
        
        switch (ordenacao != null ? ordenacao.toLowerCase() : "nome") {
            case "avaliacao" -> restaurantes = restauranteRepository.findByAtivoOrderByAvaliacaoDesc(filtroAtivo);
            case "taxa" -> restaurantes = restauranteRepository.findByAtivoOrderByTaxaEntregaAsc(filtroAtivo);
            default -> restaurantes = restauranteRepository.findByAtivoOrderByNomeAsc(filtroAtivo);
        }
        
        return restaurantes.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca avançada com múltiplos filtros
     */
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarAvancado(FiltroRestauranteDTO filtro) {
        logger.info("Busca avançada com filtros: {}", filtro);
        
        List<Restaurante> restaurantes = restauranteRepository.findWithFilters(
            filtro.getNome(),
            filtro.getCategoria(), 
            filtro.getEndereco(),
            filtro.getAtivo()
        );
        
        // Aplicar filtros adicionais que não estão na query
        if (filtro.getAvaliacaoMinima() != null) {
            restaurantes = restaurantes.stream()
                    .filter(r -> r.getAvaliacao() >= filtro.getAvaliacaoMinima())
                    .collect(Collectors.toList());
        }
        
        if (filtro.getTaxaMaxima() != null) {
            restaurantes = restaurantes.stream()
                    .filter(r -> r.getTaxaEntrega().compareTo(filtro.getTaxaMaxima()) <= 0)
                    .collect(Collectors.toList());
        }
        
        // Aplicar ordenação
        if (filtro.getOrdenacao() != null) {
            switch (filtro.getOrdenacao().toLowerCase()) {
                case "avaliacao_desc" -> restaurantes.sort((a, b) -> Double.compare(b.getAvaliacao(), a.getAvaliacao()));
                case "avaliacao_asc" -> restaurantes.sort((a, b) -> Double.compare(a.getAvaliacao(), b.getAvaliacao()));
                case "taxa_asc" -> restaurantes.sort((a, b) -> a.getTaxaEntrega().compareTo(b.getTaxaEntrega()));
                case "taxa_desc" -> restaurantes.sort((a, b) -> b.getTaxaEntrega().compareTo(a.getTaxaEntrega()));
                case "nome_desc" -> restaurantes.sort((a, b) -> b.getNome().compareToIgnoreCase(a.getNome()));
                default -> restaurantes.sort((a, b) -> a.getNome().compareToIgnoreCase(b.getNome()));
            }
        }
        
        return restaurantes.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca restaurante por ID
     */
    @Transactional(readOnly = true)
    public RestauranteResponseDTO buscarPorId(Long id) {
        logger.info("Buscando restaurante por ID: {}", id);
        
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", id));
        
        return convertToResponseDTO(restaurante);
    }
    
    /**
     * Atualiza um restaurante
     */
    public RestauranteResponseDTO atualizar(Long id, RestauranteDTO dto) {
        logger.info("Atualizando restaurante ID {}: {}", id, dto.getNome());
        
        Restaurante restauranteExistente = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", id));
        
        // Validações de negócio
        validarRestauranteDTO(dto);
        
        // Normalizar categoria
        dto.normalizarCategoria();
        
        // Validar categoria
        if (!dto.isCategoriaValida()) {
            throw new BusinessException("Categoria inválida: " + dto.getCategoria());
        }
        
        // Atualizar campos
        restauranteExistente.setNome(dto.getNome());
        restauranteExistente.setCategoria(dto.getCategoria());
        restauranteExistente.setEndereco(dto.getEndereco());
        restauranteExistente.setTaxaEntrega(dto.getTaxaEntrega());
        
        if (dto.getAvaliacao() != null) {
            restauranteExistente.setAvaliacao(dto.getAvaliacao());
        }
        
        Restaurante restauranteAtualizado = restauranteRepository.save(restauranteExistente);
        
        logger.info("Restaurante atualizado com sucesso: ID {}", id);
        return convertToResponseDTO(restauranteAtualizado);
    }
    
    /**
     * Alterna o status ativo/inativo do restaurante
     */
    public RestauranteResponseDTO alterarStatus(Long id) {
        logger.info("Alterando status do restaurante ID: {}", id);
        
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", id));
        
        boolean statusAnterior = restaurante.isAtivo();
        restaurante.setAtivo(!statusAnterior);
        
        Restaurante restauranteAtualizado = restauranteRepository.save(restaurante);
        
        logger.info("Status do restaurante ID {} alterado de {} para {}", 
                   id, statusAnterior, restauranteAtualizado.isAtivo());
        
        return convertToResponseDTO(restauranteAtualizado);
    }
    
    /**
     * Calcula taxa de entrega para um CEP específico (usa o novo serviço)
     */
    @Transactional(readOnly = true)
    private TaxaEntregaResponse calcularTaxaEntregaDetalhada(Long restauranteId, String cep) {
        return taxaEntregaService.calcularTaxa(restauranteId, cep);
    }

    @Override
    public RestauranteResponseDTO cadastrarRestaurante(RestauranteDTO dto) {
        return criar(dto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public RestauranteResponseDTO buscarRestaurantePorId(Long id) {
        return buscarPorId(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarRestaurantesPorCategoria(String categoria) {
        return listarTodos(null, categoria, null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarRestaurantesDisponiveis() {
        return listarTodos(null, null, true);
    }
    
    @Override
    public RestauranteResponseDTO atualizarRestaurante(Long id, RestauranteDTO dto) {
        return atualizar(id, dto);
    }
    
    @Override
    public BigDecimal calcularTaxaEntrega(Long restauranteId, String cep) {
        // Método legado - usa o novo método e retorna apenas a taxa
        TaxaEntregaResponse response = calcularTaxaEntregaCompleta(restauranteId, cep);
        return response.getTaxaEntrega();
    }
    
    /**
     * Método novo que retorna resposta completa
     */
    public TaxaEntregaResponse calcularTaxaEntregaCompleta(Long restauranteId, String cep) {
        return calcularTaxaEntregaDetalhada(restauranteId, cep);
    }
    
    @Override
    public RestauranteResponseDTO ativarDesativarRestaurante(Long id) {
        return alterarStatus(id);
    }
    
    /**
     * Obtém estatísticas dos restaurantes
     */
    @Transactional(readOnly = true)
    public EstatisticasRestauranteDTO obterEstatisticas() {
        logger.info("Obtendo estatísticas dos restaurantes");
        
        long totalRestaurantes = restauranteRepository.count();
        long restaurantesAtivos = restauranteRepository.countByAtivo(true);
        long restaurantesInativos = totalRestaurantes - restaurantesAtivos;
        long totalCategorias = restauranteRepository.countCategoriasAtivas();
        
        // Calcular avaliação média
        List<Restaurante> todosRestaurantes = restauranteRepository.findAll();
        double avaliacaoMedia = todosRestaurantes.stream()
                .filter(r -> r.getAvaliacao() != null)
                .mapToDouble(Restaurante::getAvaliacao)
                .average()
                .orElse(0.0);
        
        // Calcular taxa média
        double taxaMedia = todosRestaurantes.stream()
                .filter(r -> r.getTaxaEntrega() != null)
                .mapToDouble(r -> r.getTaxaEntrega().doubleValue())
                .average()
                .orElse(0.0);
        
        return new EstatisticasRestauranteDTO(
                totalRestaurantes,
                restaurantesAtivos,
                restaurantesInativos,
                totalCategorias,
                avaliacaoMedia,
                taxaMedia
        );
    }
    
    /**
     * Obtém lista de categorias disponíveis
     */
    @Transactional(readOnly = true)
    public List<String> obterCategorias() {
        logger.info("Obtendo lista de categorias");
        
        return restauranteRepository.findAll().stream()
                .map(Restaurante::getCategoria)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    /**
     * Obtém sugestões de busca baseadas no texto
     */
    @Transactional(readOnly = true)
    public List<String> obterSugestoes(String texto) {
        logger.info("Obtendo sugestões para: {}", texto);
        
        if (texto == null || texto.trim().length() < 2) {
            return List.of();
        }
        
        String textoLower = texto.toLowerCase().trim();
        
        return restauranteRepository.findAll().stream()
                .filter(Restaurante::isAtivo)
                .flatMap(r -> List.of(r.getNome(), r.getCategoria()).stream())
                .distinct()
                .filter(s -> s.toLowerCase().contains(textoLower))
                .sorted()
                .limit(10)
                .collect(Collectors.toList());
    }
    
    /**
     * Verifica se existe restaurante com nome similar
     */
    @Transactional(readOnly = true)
    public boolean existeNomeSimilar(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        
        String nomeLimpo = nome.trim().toLowerCase();
        
        return restauranteRepository.findAll().stream()
                .anyMatch(r -> r.getNome().toLowerCase().equals(nomeLimpo));
    }
    
    // Métodos utilitários privados
    
    /**
     * Valida os dados do RestauranteDTO
     */
    private void validarRestauranteDTO(RestauranteDTO dto) {
        if (!dto.isValido()) {
            throw new BusinessException("Dados do restaurante são inválidos");
        }
        
        if (dto.getTaxaEntrega().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Taxa de entrega não pode ser negativa");
        }
        
        if (dto.getTaxaEntrega().compareTo(new BigDecimal("999.99")) > 0) {
            throw new BusinessException("Taxa de entrega não pode ser maior que R$ 999,99");
        }
        
        if (dto.getAvaliacao() != null && (dto.getAvaliacao() < 1.0 || dto.getAvaliacao() > 5.0)) {
            throw new BusinessException("Avaliação deve estar entre 1.0 e 5.0");
        }
    }
    
    /**
     * Converte DTO para entidade
     */
    private Restaurante convertToEntity(RestauranteDTO dto) {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.getNome());
        restaurante.setCategoria(dto.getCategoria());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setTaxaEntrega(dto.getTaxaEntrega());
        restaurante.setAvaliacao(dto.getAvaliacao() != null ? dto.getAvaliacao() : 5.0);
        return restaurante;
    }
    
    /**
     * Converte entidade para DTO de resposta
     */
    private RestauranteResponseDTO convertToResponseDTO(Restaurante restaurante) {
        RestauranteResponseDTO dto = new RestauranteResponseDTO();
        dto.setId(restaurante.getId());
        dto.setNome(restaurante.getNome());
        dto.setCategoria(restaurante.getCategoria());
        dto.setEndereco(restaurante.getEndereco());
        dto.setTaxaEntrega(restaurante.getTaxaEntrega());
        dto.setAvaliacao(restaurante.getAvaliacao());
        dto.setAtivo(restaurante.isAtivo());
        
        // Definir timestamps se disponíveis na entidade
        try {
            java.lang.reflect.Field criadoEmField = restaurante.getClass().getDeclaredField("criadoEm");
            criadoEmField.setAccessible(true);
            LocalDateTime criadoEm = (LocalDateTime) criadoEmField.get(restaurante);
            dto.setCriadoEm(criadoEm);
        } catch (Exception e) {
            // Campo não existe, mantém null
        }
        
        try {
            java.lang.reflect.Field atualizadoEmField = restaurante.getClass().getDeclaredField("atualizadoEm");
            atualizadoEmField.setAccessible(true);
            LocalDateTime atualizadoEm = (LocalDateTime) atualizadoEmField.get(restaurante);
            dto.setAtualizadoEm(atualizadoEm);
        } catch (Exception e) {
            // Campo não existe, mantém null
        }
        
        return dto;
    }
    

    
    // Métodos legados para compatibilidade (deprecated)
    @Deprecated
    public Restaurante cadastrar(Restaurante restaurante) {
        return restauranteRepository.save(restaurante);
    }
    
    @Deprecated
    public Restaurante buscarPorIdLegado(Long id) {
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Restaurante não encontrado"));
    }
    
    @Deprecated
    public List<Restaurante> listarTodos() {
        return restauranteRepository.findAll();
    }
    
    @Deprecated
    public List<Restaurante> buscarPorCategoriaLegado(String categoria) {
        return restauranteRepository.findByCategoria(categoria);
    }
    
    @Deprecated
    public List<Restaurante> buscarPorNomeLegado(String nome) {
        return restauranteRepository.findTop5ByOrderByNomeAsc();
    }
    
    @Deprecated
    public Restaurante atualizarLegado(Long id, Restaurante restaurante) {
        Restaurante restauranteExistente = buscarPorIdLegado(id);
        
        restauranteExistente.setNome(restaurante.getNome());
        restauranteExistente.setCategoria(restaurante.getCategoria());
        restauranteExistente.setEndereco(restaurante.getEndereco());
        restauranteExistente.setTaxaEntrega(restaurante.getTaxaEntrega());
        restauranteExistente.setAvaliacao(restaurante.getAvaliacao());
        
        return restauranteRepository.save(restauranteExistente);
    }
    
    @Deprecated
    public void alterarStatusLegado(Long id, boolean ativo) {
        Restaurante restaurante = buscarPorIdLegado(id);
        restaurante.setAtivo(ativo);
        restauranteRepository.save(restaurante);
    }
    
    @Deprecated
    public List<Restaurante> listarPorAvaliacao() {
        return restauranteRepository.findByAtivoTrue();
    }
}