package br.com.erivan.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.erivan.domain.Orcamento;
import br.com.erivan.domain.StatusOrcamento;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

	@Query("select o from Orcamento o where o.nome like %?1%")
	List<Orcamento> findOrcamentoByName(String nome);

	//busca por periodo no listar orçamento
	@Query("select o from Orcamento o where o.dataOrcamento BETWEEN :dataInicio AND :dataFim")
	List<Orcamento> findOrcamentoByDate(Date dataInicio, Date dataFim);

	
	//busca pelo status do orcamento no listar enum
	List<Orcamento> findOrcamentoByStatusOrcamento(StatusOrcamento statusOrcamento);



}
