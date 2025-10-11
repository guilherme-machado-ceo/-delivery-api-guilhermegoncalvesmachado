package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Restaurante;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    
    // Queries básicas existentes
    List<Restaurante> findByCategoria(String categoria);
    List<Restaurante> findByAtivoTrue();
    List<Restaurante> findByTaxaEntregaLessThanEqual(BigDecimal taxa);
    List<Restaurante> findTop5ByOrderByNomeAsc();
    
    // Novas queries para busca e filtros
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);
    List<Restaurante> findByEnderecoContainingIgnoreCase(String endereco);
    List<Restaurante> findByCategoriaIgnoreCase(String categoria);
    List<Restaurante> findByAtivoAndCategoria(Boolean ativo, String categoria);
    List<Restaurante> findByAtivoAndCategoriaIgnoreCase(Boolean ativo, String categoria);
    
    // Queries com múltiplos filtros
    @Query("SELECT r FROM Restaurante r WHERE " +
           "(:nome IS NULL OR LOWER(r.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
           "(:categoria IS NULL OR LOWER(r.categoria) LIKE LOWER(CONCAT('%', :categoria, '%'))) AND " +
           "(:endereco IS NULL OR LOWER(r.endereco) LIKE LOWER(CONCAT('%', :endereco, '%'))) AND " +
           "(:ativo IS NULL OR r.ativo = :ativo)")
    List<Restaurante> findWithFilters(@Param("nome") String nome, 
                                     @Param("categoria") String categoria,
                                     @Param("endereco") String endereco,
                                     @Param("ativo") Boolean ativo);
    
    // Busca por texto em múltiplos campos
    @Query("SELECT r FROM Restaurante r WHERE " +
           "LOWER(r.nome) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(r.categoria) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(r.endereco) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Restaurante> findByTextoGeral(@Param("texto") String texto);
    
    // Busca por texto com filtro de status
    @Query("SELECT r FROM Restaurante r WHERE " +
           "(LOWER(r.nome) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(r.categoria) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(r.endereco) LIKE LOWER(CONCAT('%', :texto, '%'))) AND " +
           "(:ativo IS NULL OR r.ativo = :ativo)")
    List<Restaurante> findByTextoGeralAndAtivo(@Param("texto") String texto, @Param("ativo") Boolean ativo);
    
    // Queries por avaliação
    List<Restaurante> findByAvaliacaoGreaterThanEqual(Double avaliacao);
    List<Restaurante> findByAvaliacaoGreaterThanEqualAndAtivo(Double avaliacao, Boolean ativo);
    
    // Queries por taxa de entrega
    List<Restaurante> findByTaxaEntregaBetween(BigDecimal taxaMin, BigDecimal taxaMax);
    List<Restaurante> findByTaxaEntregaBetweenAndAtivo(BigDecimal taxaMin, BigDecimal taxaMax, Boolean ativo);
    
    // Queries ordenadas
    List<Restaurante> findByAtivoOrderByNomeAsc(Boolean ativo);
    List<Restaurante> findByAtivoOrderByAvaliacaoDesc(Boolean ativo);
    List<Restaurante> findByAtivoOrderByTaxaEntregaAsc(Boolean ativo);
    
    // Query para estatísticas
    @Query("SELECT r, SUM(p.valorTotal) as totalVendas FROM Restaurante r LEFT JOIN Pedido p ON p.restaurante = r GROUP BY r")
    List<Object[]> getTotalVendasPorRestaurante();
    
    // Contadores
    long countByAtivo(Boolean ativo);
    long countByCategoria(String categoria);
    
    @Query("SELECT COUNT(DISTINCT r.categoria) FROM Restaurante r WHERE r.ativo = true")
    long countCategoriasAtivas();
}