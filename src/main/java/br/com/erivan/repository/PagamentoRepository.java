package br.com.erivan.repository;



import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.erivan.domain.Pagamento;




public interface PagamentoRepository extends JpaRepository<Pagamento, Long>{
	
	@Query("select p from Pagamento p where p.titulo like %?1%")
	List<Pagamento> findByTitulo(String titulo);
	
	@Query("select p from Pagamento p where p.cliente.id = ?1")
	List<Pagamento> buscarPorCliente(Long id);
	
	@Query("select p from Pagamento p where p.servico.id = ?1")
	List<Pagamento> buscarPorServico(Long id);
	
	//busca por periodo na lista pagamento
	@Query("select o from Pagamento o where o.dataPagamento BETWEEN :dataInicio AND :dataFim")
	List<Pagamento> findPagamentoByDate(Date dataInicio, Date dataFim);
	
	//busca pelo status do pagamento no listar enum
	//List<Pagamento> findPagamentoByStatusPagamento(StatusPagamento StatusPagamento);
	
	
}
