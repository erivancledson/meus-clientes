package br.com.erivan.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.erivan.domain.Chamado;
import br.com.erivan.domain.Usuario;

public interface ChamadoRepository extends JpaRepository<Chamado, Long>{


	@Query("select c from Chamado c where c.usuario.id = ?1")
	List<Chamado> findByUsuarioId(Long id);
	
	
	

}
