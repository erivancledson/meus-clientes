package br.com.erivan.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.erivan.domain.Chamado;
import br.com.erivan.domain.StatusChamado;
import br.com.erivan.repository.ChamadoRepository;

@Service
public class ChamadoService {

	@Autowired
	ChamadoRepository repository;
	
	@Transactional(readOnly = false)
	public void salvarChamado(Chamado chamado) {
		Date date = new Date();
		chamado.setDataChamado(date);
		chamado.setStatusChamado(StatusChamado.ABERTO);//seta o chamado
		repository.save(chamado);
	}
	
	@Transactional(readOnly = true)
	public List<Chamado> buscarTodos(){
		return repository.findAll();
	}
	
	//para o editar
	@Transactional(readOnly = true)
	public Chamado buscarPorId(Long id) {
		return repository.findById(id).get();
	}
	
	@Transactional(readOnly = false)
	public void editarChamado(Chamado chamado) {
		Date date = new Date();
		chamado.setDataChamado(date);
		repository.save(chamado);
	}
	
	// chamados por usuario
	public List<Chamado> findByUsuarioId(Long id) {
		return repository.findByUsuarioId(id);
	}


}
