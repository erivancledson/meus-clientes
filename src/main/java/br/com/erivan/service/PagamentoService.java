package br.com.erivan.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.erivan.domain.Pagamento;
import br.com.erivan.repository.PagamentoRepository;

@Service
public class PagamentoService {
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Transactional(readOnly = false)
	public void salvarPagamento(Pagamento pagamento) {
	
		pagamentoRepository.save(pagamento);
	}
	
	@Transactional(readOnly = false)
	public void editarPagamento(Pagamento pagamento) {
		pagamentoRepository.save(pagamento);
	}
	
	@Transactional(readOnly = false)
	public void excluir(Long id) {
		pagamentoRepository.deleteById(id);
	}
	
	@Transactional(readOnly = true)
	public Pagamento buscarPorId(Long id) {
		return pagamentoRepository.findById(id).get();
	}
	
	@Transactional(readOnly = true)
	public List<Pagamento> buscarTodos(){
		return pagamentoRepository.findAll();
	}


	

}
