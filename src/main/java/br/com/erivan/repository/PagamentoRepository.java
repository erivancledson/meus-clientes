package br.com.erivan.repository;



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


	
}
