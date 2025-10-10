import axios from 'axios';
import { Cliente, ClienteDTO } from '../types/Cliente';

const API_BASE_URL = 'http://localhost:8080/api';

export const clienteService = {
  // Listar todos os clientes ativos
  async listarClientes(): Promise<Cliente[]> {
    const response = await axios.get(`${API_BASE_URL}/clientes`);
    return response.data;
  },

  // Buscar cliente por ID
  async buscarPorId(id: number): Promise<Cliente> {
    const response = await axios.get(`${API_BASE_URL}/clientes/${id}`);
    return response.data;
  },

  // Buscar cliente por email
  async buscarPorEmail(email: string): Promise<Cliente> {
    const response = await axios.get(`${API_BASE_URL}/clientes/email/${email}`);
    return response.data;
  },

  // Cadastrar novo cliente
  async cadastrar(clienteData: ClienteDTO): Promise<Cliente> {
    const response = await axios.post(`${API_BASE_URL}/clientes`, clienteData);
    return response.data;
  },

  // Atualizar cliente
  async atualizar(id: number, clienteData: ClienteDTO): Promise<Cliente> {
    const response = await axios.put(`${API_BASE_URL}/clientes/${id}`, clienteData);
    return response.data;
  },

  // Ativar/Desativar cliente
  async alterarStatus(id: number): Promise<Cliente> {
    const response = await axios.patch(`${API_BASE_URL}/clientes/${id}/status`);
    return response.data;
  }
};