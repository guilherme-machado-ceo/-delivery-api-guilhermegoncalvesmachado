import axios from 'axios';
import { Restaurante, RestauranteDTO, TaxaEntregaResponse } from '../types/Restaurante';

const API_BASE_URL = 'http://localhost:8082/api';

export const restauranteService = {
  // Listar todos os restaurantes disponíveis
  async listarRestaurantes(): Promise<Restaurante[]> {
    const response = await axios.get(`${API_BASE_URL}/restaurantes`);
    return response.data;
  },

  // Buscar restaurante por ID
  async buscarPorId(id: number): Promise<Restaurante> {
    const response = await axios.get(`${API_BASE_URL}/restaurantes/${id}`);
    return response.data;
  },

  // Buscar restaurantes por categoria
  async buscarPorCategoria(categoria: string): Promise<Restaurante[]> {
    const response = await axios.get(`${API_BASE_URL}/restaurantes/categoria/${categoria}`);
    return response.data;
  },

  // Cadastrar novo restaurante
  async cadastrar(restauranteData: RestauranteDTO): Promise<Restaurante> {
    const response = await axios.post(`${API_BASE_URL}/restaurantes`, restauranteData);
    return response.data;
  },

  // Atualizar restaurante
  async atualizar(id: number, restauranteData: RestauranteDTO): Promise<Restaurante> {
    const response = await axios.put(`${API_BASE_URL}/restaurantes/${id}`, restauranteData);
    return response.data;
  },

  // Ativar/Desativar restaurante
  async alterarStatus(id: number): Promise<Restaurante> {
    const response = await axios.patch(`${API_BASE_URL}/restaurantes/${id}/status`);
    return response.data;
  },

  // Calcular taxa de entrega
  async calcularTaxaEntrega(restauranteId: number, cep: string): Promise<TaxaEntregaResponse> {
    const response = await axios.get(`${API_BASE_URL}/restaurantes/${restauranteId}/taxa-entrega/${cep}`);
    return response.data;
  },

  // Obter categorias disponíveis
  async obterCategorias(): Promise<string[]> {
    return [
      'Pizzaria',
      'Hamburgueria', 
      'Japonesa',
      'Italiana',
      'Brasileira',
      'Mexicana',
      'Chinesa',
      'Árabe',
      'Vegetariana',
      'Doces & Sobremesas',
      'Lanches',
      'Saudável'
    ];
  }
};