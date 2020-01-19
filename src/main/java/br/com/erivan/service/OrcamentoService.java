package br.com.erivan.service;


import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.erivan.domain.Orcamento;
import br.com.erivan.domain.StatusOrcamento;
import br.com.erivan.repository.OrcamentoRepository;

@Service
public class OrcamentoService {
	
	@Autowired
	OrcamentoRepository repository;
	
	@Transactional(readOnly = false)
	public void salvarOrcamento(Orcamento orcamento) {
		//salva com a data atual
		Date date = new Date();
		orcamento.setDataOrcamento(date);
		
		orcamento.setStatusOrcamento(StatusOrcamento.AGUARDANDO_ANALISE); //seta como aguardando analise
		repository.save(orcamento);
	}

	@Transactional(readOnly = true)
	public List<Orcamento> buscarTodos() {
		
		return repository.findAll();
	}

	@Transactional(readOnly = false)
	public void excluir(Long id) {
		repository.deleteById(id);
		
	}
	//para o editar
	@Transactional(readOnly = true)
	public Orcamento buscarPorid(Long id) {
		
		return repository.findById(id).get();
	}

	@Transactional(readOnly = false)
	public void editarOrcamento(Orcamento orcamento) {
		//salva com a data atual
		Date date = new Date();
		orcamento.setDataOrcamento(date);
		repository.save(orcamento);
		
	}
	

}
