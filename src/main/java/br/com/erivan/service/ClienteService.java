package br.com.erivan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.erivan.domain.Cliente;
import br.com.erivan.domain.Usuario;
import br.com.erivan.repository.ClienteRepository;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Transactional(readOnly = false)
	public void salvarCliente(Cliente cliente) {
		clienteRepository.save(cliente);
	}
	
	@Transactional(readOnly = false)
	public void editarCliente(Cliente cliente) {
		clienteRepository.save(cliente);
	}
	
	@Transactional(readOnly = false)
	public void excluir(Long id) {
		clienteRepository.deleteById(id);
	}
	
	@Transactional(readOnly = true)
	public Cliente buscarPorId(Long id) {
		return clienteRepository.findById(id).get();
	}
	
	@Transactional(readOnly = true)
	public List<Cliente> buscarTodos(){
		return clienteRepository.findAll();
	}
	//verifica se tem pagamentos associados ao id
	public boolean clienteTemPagamentos(Long id) {
		if(buscarPorId(id).getPagamentos().isEmpty()) {
			return false;
		}
		return true;
	}
	



}
