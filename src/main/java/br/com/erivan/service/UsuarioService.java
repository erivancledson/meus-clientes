package br.com.erivan.service;

import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import br.com.erivan.domain.Perfil;
import br.com.erivan.domain.PerfilTipo;
import br.com.erivan.domain.Usuario;
import br.com.erivan.exception.AcessoNegadoException;
import br.com.erivan.repository.UsuarioRepository;

@Service
public class UsuarioService  implements UserDetailsService{
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	private EmailService emailService;
	
	
	//busca o email que foi digitado no email para verificacao
	@Transactional(readOnly = true)
	public Usuario buscarPorEmail(String email) {
		return repository.findByEmail(email);
		
	}
	
	//pega as credenciais, consulta no banco de dados. E cria um perfil aonde é autorizado pela security config
	@Override @Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = buscarPorEmailEAtivo(username)   //verifica se o usuario estar ativo e o email existe
				.orElseThrow(() -> new UsernameNotFoundException("Usuario " + username + " não encontrado."));
		return new User(
					usuario.getEmail(),
					usuario.getSenha(),
					AuthorityUtils.createAuthorityList(getAtuthorities(usuario.getPerfis()))
				);
	}
	

	
	//perfis do usuario que foi retornado pelo user name
	private String[] getAtuthorities(List<Perfil> perfis) {
		String[] authorities = new String[perfis.size()];
		for (int i = 0; i < perfis.size(); i++) {
			authorities[i] = perfis.get(i).getDesc();
		}
		return authorities;
	}
	
	
	//deleta o usuario
	@Transactional(readOnly = false)
	public void excluir(Long id) {
		repository.deleteById(id);
	}
	
	
	@Transactional(readOnly = true)
	public Usuario buscarPorId(Long id) {
		
		return repository.findById(id).get();
	}

	@Transactional(readOnly = false)
	public void salvarUsuario(Usuario usuario) {
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);
		repository.save(usuario);
		
	}
	@Transactional(readOnly = true)
	public List<Usuario> buscarTodos() {
		
		return repository.findAll();
	}

	@Transactional(readOnly = false)
	public void editarUsuario(@Valid Usuario usuario) {
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);
		repository.save(usuario);
		
	}
	
	//cadastro de clientes
	@Transactional(readOnly = false)
	public void salvarCadastroCliente(Usuario usuario) throws MessagingException{
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);
		usuario.addPerfil(PerfilTipo.CLIENTE);
		repository.save(usuario);
		
		//quando cadastrar envia o email
		emailDeConfirmacaoDeCadastro(usuario.getEmail());
	}
	
	//usuario ativo
	@Transactional(readOnly = true)
	public Optional<Usuario> buscarPorEmailEAtivo(String email){
		return repository.findByEmailAtivo(email);
	}
	
	//chama o enviar do emails service
	public void emailDeConfirmacaoDeCadastro(String email) throws MessagingException {
		//tansforma o email em base 64
		String codigo = Base64Utils.encodeToString(email.getBytes());
		emailService.enviarPedidoDeConfirmacaoDeCadastro(email, codigo);
	}
	
	@Transactional(readOnly = false)
	public void ativarCadastroCliente(String codigo) {
		//decodifica o codigo que foi enviado em base64
				String email = new String(Base64Utils.decodeFromString(codigo));
				Usuario usuario = buscarPorEmail(email); //ver se o usuario tem o email cadastrado
				if(usuario.hasNotId()) { //se não tem o id entra no if
					throw new AcessoNegadoException("Não foi possível ativar seu cadastro. Entre em contato com o suporte");
				}
				//se tem o id o ativo passa a ser 1
				usuario.setAtivo(true);
		
	}

	//redifinicao de senha por email
	@Transactional(readOnly = false)
	public void pedidoRedefinicaoDeSenha(String email) throws MessagingException{
		Usuario usuario = buscarPorEmailEAtivo(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario " + email + " não encontrado."));
	
				//gera uma string com 6 caracteres entre letras e numeros
				String verificador = RandomStringUtils.randomAlphanumeric(6);
				
			     usuario.setCodigoVerificador(verificador);
				
				//faz o envio do codigo verificador para o email do usuario
				emailService.enviarPedidoRedefinicaoSenha(email, verificador);
	}

	//alterar senha por email
	@Transactional(readOnly = false)
	public void alterarSenha(Usuario usuario, String senha) {
		usuario.setSenha(new BCryptPasswordEncoder().encode(senha)); //pega a senha e salva
		repository.save(usuario);		
	}

	

}
