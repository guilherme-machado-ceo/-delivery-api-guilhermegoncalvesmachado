export interface Restaurante {
  id: number;
  nome: string;
  categoria: string;
  endereco: string;
  taxaEntrega: number;
  avaliacao: number;
  ativo: boolean;
}

export interface RestauranteDTO {
  nome: string;
  categoria: string;
  endereco: string;
  taxaEntrega: number;
  avaliacao?: number;
}

export interface RestauranteFormData {
  nome: string;
  categoria: string;
  endereco: string;
  taxaEntrega: number;
  avaliacao: number;
}

export interface TaxaEntregaRequest {
  restauranteId: number;
  cep: string;
}

export interface TaxaEntregaResponse {
  taxaEntrega: number;
  distancia: number;
  tempoEstimado: string;
}