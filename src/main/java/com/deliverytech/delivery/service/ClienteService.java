package com.deliverytech.delivery.service;

import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    public Cliente cadastrar(Cliente cliente) {
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        return clienteRepository.save(cliente);
    }
    
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    }
    
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
    
    public Cliente atualizar(Long id, Cliente cliente) {
        Cliente clienteExistente = buscarPorId(id);
        
        // Verifica se o novo email já existe para outro cliente
        Optional<Cliente> clienteComEmail = clienteRepository.findByEmail(cliente.getEmail());
        if (clienteComEmail.isPresent() && !clienteComEmail.get().getId().equals(id)) {
            throw new IllegalArgumentException("Email já cadastrado para outro cliente");
        }
        
        clienteExistente.setNome(cliente.getNome());
        clienteExistente.setEmail(cliente.getEmail());
        clienteExistente.setTelefone(cliente.getTelefone());
        clienteExistente.setEndereco(cliente.getEndereco());
        
        return clienteRepository.save(clienteExistente);
    }
    
    public void inativar(Long id) {
        Cliente cliente = buscarPorId(id);
        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }
    
    public List<Cliente> buscarAtivos() {
        return clienteRepository.findByAtivo(true);
    }
}