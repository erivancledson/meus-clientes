package br.com.erivan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.erivan.domain.PerfilTipo;
import br.com.erivan.service.UsuarioService;

@EnableGlobalMethodSecurity(prePostEnabled = true) //habilita o uso de anotações para a parte de segurança, depois vai nos controller e passa as permissoes que cada usuario tem
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
	private static final String ATENDENTE = PerfilTipo.ATENDENTE.getDesc();
	private static final String CLIENTE = PerfilTipo.CLIENTE.getDesc();
	
	@Autowired
	private UsuarioService service;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
		.antMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll() //liberando arquivos estaticos
		.antMatchers("/usuarios/p/**").permitAll()  //redefinir senha
		.antMatchers("/orcamentos/novo/cadastro").permitAll()
		.antMatchers("/orcamentos/cadastro/salvar").permitAll()
		.antMatchers("/usuarios/novo/cadastro", "/usuarios/cadastro/realizado", "/usuarios/cadastro/cliente/salvar").permitAll() //cadastro de cliente
		.antMatchers("/usuarios/confirmacao/cadastro").permitAll() //envio de email
		.antMatchers("/usuarios/editar/senha").permitAll()
		.antMatchers("/usuarios/confirmar/senha").permitAll()
		.antMatchers("/orcamentos/**").hasAuthority(ADMIN)
		.antMatchers("/usuarios/**").hasAuthority(ADMIN) //admin do banco de dados
		.antMatchers("/servicos/**").hasAnyAuthority(ADMIN, ATENDENTE)
		.antMatchers("/clientes/**").hasAnyAuthority(ADMIN, ATENDENTE)
		.antMatchers("/chamados/salvar").hasAuthority(CLIENTE)
		
		
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/login") //pagina de login
		.defaultSuccessUrl("/", true) //passou com sucesso
		.failureUrl("/login-error") //do homeController
		.permitAll() //tem acesso a pagina de login e de error
		.and()
			.logout()
			.logoutSuccessUrl("/login") //fez o logout vai para login
		.and()
			.exceptionHandling() //captura o acesso negado
			.accessDeniedPage("/acesso-negado") //do homeController
			.and()
			.rememberMe();  //recurso de lembre me que fica abaixo do login, ele pedi lá no html o name e o id iguais

		
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//tipo de criptografia Bcrypt
		//compara a senha com a senha do formulario de login
		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
		
	}
	
	

}
