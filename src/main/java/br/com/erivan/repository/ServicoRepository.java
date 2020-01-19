package br.com.erivan.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.erivan.domain.Servico;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
	
	@Query("select s from Servico s where s.nome like %?1%")
	List<Servico> findServicoByName(String nome);


}
