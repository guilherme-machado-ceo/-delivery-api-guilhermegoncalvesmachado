export interface Cliente {
  id: number;
  nome: string;
  email: string;
  telefone: string;
  endereco: string;
  ativo: boolean;
  dataCadastro: string;
}

export interface ClienteDTO {
  nome: string;
  email: string;
  telefone: string;
  endereco: string;
}

export interface ClienteFormData {
  nome: string;
  email: string;
  telefone: string;
  endereco: string;
}