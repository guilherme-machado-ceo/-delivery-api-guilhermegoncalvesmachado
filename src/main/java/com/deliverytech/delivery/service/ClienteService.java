package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ClienteDTO;
import com.deliverytech.delivery.dto.ClienteResponseDTO;
import com.deliverytech.delivery.exception.DuplicateResourceException;
import com.deliverytech.delivery.exception.EntityNotFoundException;
import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.repository.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteService implements ClienteServiceInterface {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public ClienteResponseDTO cadastrarCliente(ClienteDTO dto) {
        // Validar email único
        if (clienteRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Cliente", "email", dto.getEmail());
        }
        
        Cliente cliente = modelMapper.map(dto, Cliente.class);
        cliente.setAtivo(true);
        
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return modelMapper.map(clienteSalvo, ClienteResponseDTO.class);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente", id));
        
        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarClientePorEmail(String email) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com email: " + email));
        
        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }
    
    @Override
    public ClienteResponseDTO atualizarCliente(Long id, ClienteDTO dto) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente", id));
        
        // Validar email único (se mudou)
        if (!clienteExistente.getEmail().equals(dto.getEmail())) {
            if (clienteRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new DuplicateResourceException("Cliente", "email", dto.getEmail());
            }
        }
        
        // Atualizar campos
        clienteExistente.setNome(dto.getNome());
        clienteExistente.setEmail(dto.getEmail());
        clienteExistente.setTelefone(dto.getTelefone());
        clienteExistente.setEndereco(dto.getEndereco());
        
        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);
        return modelMapper.map(clienteAtualizado, ClienteResponseDTO.class);
    }
    
    @Override
    public ClienteResponseDTO ativarDesativarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente", id));
        
        cliente.setAtivo(!cliente.isAtivo());
        
        Cliente clienteAtualizado = clienteRepository.save(cliente);
        return modelMapper.map(clienteAtualizado, ClienteResponseDTO.class);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarClientesAtivos() {
        List<Cliente> clientesAtivos = clienteRepository.findByAtivoTrue();
        
        return clientesAtivos.stream()
                .map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class))
                .collect(Collectors.toList());
    }
    
    // Métodos legados para compatibilidade (deprecated)
    @Deprecated
    public Cliente cadastrar(Cliente cliente) {
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        return clienteRepository.save(cliente);
    }
    
    @Deprecated
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Cliente não encontrado"));
    }
    
    @Deprecated
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
    
    @Deprecated
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
    
    @Deprecated
    public void inativar(Long id) {
        Cliente cliente = buscarPorId(id);
        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }
    
    @Deprecated
    public List<Cliente> buscarAtivos() {
        return clienteRepository.findByAtivoTrue();
    }
}