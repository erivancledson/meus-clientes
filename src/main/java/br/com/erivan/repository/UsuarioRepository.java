package br.com.erivan.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.erivan.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
   @Query("select u from Usuario u where u.email like %?1%")
	Usuario findByEmail(@Param("email") String email);
   
   @Query("select u from Usuario u where u.email like :email AND u.ativo = true")
   Optional<Usuario> findByEmailAtivo(String email);

   //para pegar o usuario atual, do chamado
   @Query("select u from Usuario u where u.email like :email")
   Optional<Usuario> findByUsuarioEmail(String email);

}
