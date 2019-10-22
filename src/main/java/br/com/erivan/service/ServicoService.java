package br.com.erivan.service;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.erivan.domain.Servico;
import br.com.erivan.repository.ServicoRepository;

@Service
public class ServicoService{

	@Autowired
	private ServicoRepository servicoRepository;
	
	@Transactional(readOnly = false)
	public void salvarServico(Servico servico) {
		servicoRepository.save(servico);
	}
	
	@Transactional(readOnly = true)
	public List<Servico> buscarTodos(){
		return servicoRepository.findAll();
	}
	
	@Transactional(readOnly = false)
	public void editarServico(Servico servico) {
		servicoRepository.save(servico);
	}
	
	@Transactional(readOnly = false)
	public void excluir(Long id) {
		servicoRepository.deleteById(id);
	}
	@Transactional(readOnly = true)
	public Servico buscarPorId(Long id) {
		return servicoRepository.findById(id).get();
	}

	//verifica se tem servico ligado a pagamentos
	public boolean servicoTemPagamentos(Long id) {
		if(buscarPorId(id).getPagamentos().isEmpty()) {
			return false;
		}
		return true;
	}


}
